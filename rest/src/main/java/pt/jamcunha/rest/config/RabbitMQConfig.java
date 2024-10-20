package pt.jamcunha.rest.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
	@Value("${rabbitmq.exchange}")
	private String exchange;

	@Value("${rabbitmq.queue.request}")
	private String requestQueue;

	@Value("${rabbitmq.queue.response}")
	private String responseQueue;

	@Value("${rabbitmq.routing-key.request}")
	private String requestRoutingKey;

	@Value("${rabbitmq.routing-key.response}")
	private String responseRoutingKey;

	@Bean
	TopicExchange exchange() {
		return new TopicExchange(exchange);
	}

	@Bean
	Queue requestQueue() {
		return new Queue(requestQueue);
	}

	@Bean
	Queue responseQueue() {
		return new Queue(responseQueue);
	}

	@Bean
	Binding requestBinding() {
		return BindingBuilder.bind(requestQueue()).to(exchange()).with(requestRoutingKey);
	}

	@Bean
	Binding responseBinding() {
		return BindingBuilder.bind(responseQueue()).to(exchange()).with(responseRoutingKey);
	}

	@Bean
	RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
		RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
		rabbitTemplate.setReplyAddress(responseQueue);
		return rabbitTemplate;
	}

	@Bean
	SimpleMessageListenerContainer container(ConnectionFactory connectionFactory) {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.setQueueNames(responseQueue);
		container.setMessageListener(rabbitTemplate(connectionFactory));
		return container;
	}
}
