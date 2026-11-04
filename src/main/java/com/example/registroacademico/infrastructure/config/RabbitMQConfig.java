package com.example.registroacademico.infrastructure.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuracion de la infraestructura de mensajeria (RabbitMQ) utilizada
 * como canal alterno de entrada para registrar estudiantes.
 */
@Configuration
public class RabbitMQConfig {

    @Value("${app.messaging.rabbitmq.exchange}")
    private String exchangeName;

    @Value("${app.messaging.rabbitmq.estudiantes.queue}")
    private String estudiantesQueueName;

    @Value("${app.messaging.rabbitmq.estudiantes.routing-key}")
    private String estudiantesRoutingKey;

    @Bean
    public DirectExchange registroAcademicoExchange() {
        return new DirectExchange(exchangeName, true, false);
    }

    @Bean
    public Queue estudiantesRegistroQueue() {
        return new Queue(estudiantesQueueName, true);
    }

    @Bean
    public Binding estudiantesRegistroBinding(Queue estudiantesRegistroQueue,
                                               DirectExchange registroAcademicoExchange) {
        return BindingBuilder.bind(estudiantesRegistroQueue)
                .to(registroAcademicoExchange)
                .with(estudiantesRoutingKey);
    }

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
                                          Jackson2JsonMessageConverter messageConverter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);
        return rabbitTemplate;
    }
}
