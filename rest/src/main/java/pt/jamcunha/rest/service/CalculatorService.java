package pt.jamcunha.rest.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import pt.jamcunha.rest.dto.OperationDTO;

@Service
public class CalculatorService {

	@Value("${rabbitmq.exchange}")
	private String exchange;

	@Value("${rabbitmq.routing-key.request}")
	private String requestRoutingKey;

	private final RabbitTemplate rabbitTemplate;

	private final ObjectMapper mapper;

	public CalculatorService(final RabbitTemplate rabbitTemplate, final ObjectMapper objectMapper) {
		this.rabbitTemplate = rabbitTemplate;
		this.mapper = objectMapper;
	}

	public BigDecimal sendOperation(final OperationDTO operationDTO) throws IOException {
		final Message message = MessageBuilder
				.withBody(mapper.writeValueAsBytes(operationDTO))
				.setCorrelationId(operationDTO.requestId())
				.build();

		final Message response = rabbitTemplate.sendAndReceive(
				exchange,
				requestRoutingKey,
				message);

		if (response == null) {
			throw new RuntimeException("No response received");
		}

		String correlationId = response.getMessageProperties().getCorrelationId();

		if (!correlationId.equals(operationDTO.requestId())) {
			throw new RuntimeException("Invalid correlation id");
		}

		final HashMap<String, Object> headers = (HashMap<String, Object>) response.getMessageProperties().getHeaders();

		if (headers.containsKey("error")) {
			String errorMessage = (String) headers.get("error");
			if (errorMessage.equals("Division by zero")) {
				throw new ArithmeticException("Division by zero");
			} else {
				throw new RuntimeException("Calculation error: " + errorMessage);
			}
		}

		return mapper.readValue(response.getBody(), BigDecimal.class);
	}
}
