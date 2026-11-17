package com.example.registroacademico.infrastructure.adapters.out.messaging;

import com.example.registroacademico.application.ports.out.EstudianteMessagePublisher;
import com.example.registroacademico.domain.models.Estudiante;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Adaptador de salida (sender) que publica en RabbitMQ la informacion
 * de un estudiante para que sea procesada de forma asincrona por el consumer.
 */
@Component
public class RabbitEstudianteMessagePublisherAdapter implements EstudianteMessagePublisher {

    private static final Logger log = LoggerFactory.getLogger(RabbitEstudianteMessagePublisherAdapter.class);

    private final RabbitTemplate rabbitTemplate;
    private final String exchange;
    private final String routingKey;

    public RabbitEstudianteMessagePublisherAdapter(RabbitTemplate rabbitTemplate,
                                                   @Value("${app.messaging.rabbitmq.exchange}") String exchange,
                                                   @Value("${app.messaging.rabbitmq.estudiantes.routing-key}") String routingKey) {
        this.rabbitTemplate = rabbitTemplate;
        this.exchange = exchange;
        this.routingKey = routingKey;
    }

    @Override
    public void publicarRegistro(Estudiante estudiante) {
        log.info("Publicando en la cola el registro del estudiante con documento: {}",
                estudiante.getCodigoEstudiantil());
        rabbitTemplate.convertAndSend(exchange, routingKey, estudiante);
    }
}