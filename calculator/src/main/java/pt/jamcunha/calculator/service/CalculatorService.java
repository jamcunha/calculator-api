package pt.jamcunha.calculator.service;

import java.io.IOException;
import java.math.BigDecimal;

import org.slf4j.MDC;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import pt.jamcunha.calculator.dto.OperationDTO;

@Service
public class CalculatorService {

	@Value("${rabbitmq.exchange}")
	private String exchange;

	@Value("${rabbitmq.routing-key.response}")
	private String responseRoutingKey;

	private final RabbitTemplate rabbitTemplate;

	public CalculatorService(final RabbitTemplate rabbitTemplate) {
		this.rabbitTemplate = rabbitTemplate;
	}

	@RabbitListener(queues = "${rabbitmq.queue.request}")
	public void processOperation(final Message message) throws IOException {
		final ObjectMapper mapper = new ObjectMapper();

		try {
			OperationDTO operation = mapper.readValue(message.getBody(), OperationDTO.class);

			MDC.put("requestId", operation.requestId());

			final BigDecimal result = calculate(operation);

			final Message response = MessageBuilder
					.withBody(mapper.writeValueAsBytes(result))
					.setCorrelationId(message.getMessageProperties().getCorrelationId())
					.build();

			final CorrelationData correlationData = new CorrelationData(
					message.getMessageProperties().getCorrelationId());

			rabbitTemplate.convertAndSend(
					exchange,
					responseRoutingKey,
					response,
					correlationData);
		} catch (ArithmeticException e) {
			sendError(message, "Division by zero");
		} catch (Exception e) {
			sendError(message, "Unexpected error");
		} finally {
			MDC.remove("requestId");
		}
	}

	private BigDecimal calculate(final OperationDTO operation) throws ArithmeticException {
		return switch (operation.operation()) {
			case "SUM" -> operation.a().add(operation.b());
			case "SUB" -> operation.a().subtract(operation.b());
			case "MUL" -> operation.a().multiply(operation.b());
			case "DIV" -> operation.a().divide(operation.b());
			default -> throw new RuntimeException("unreachable code");
		};
	}

	private void sendError(final Message message, String errorMessage) {
		final Message response = MessageBuilder
				.fromMessage(message)
				.setHeader("error", errorMessage)
				.build();

		rabbitTemplate.convertAndSend(
				exchange,
				responseRoutingKey,
				response);
	}
}
