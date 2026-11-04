package com.example.registroacademico.infrastructure.adapters.in.messaging;

import com.example.registroacademico.application.ports.in.EstudianteUseCase;
import com.example.registroacademico.domain.models.Estudiante;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * Adaptador de entrada (consumer) que escucha la cola de registro de
 * estudiantes y delega en el mismo caso de uso que utiliza el controlador
 * REST para completar el registro en base de datos.
 */
@Component
public class EstudianteRegistroConsumer {

    private static final Logger log = LoggerFactory.getLogger(EstudianteRegistroConsumer.class);

    private final EstudianteUseCase estudianteUseCase;

    public EstudianteRegistroConsumer(EstudianteUseCase estudianteUseCase) {
        this.estudianteUseCase = estudianteUseCase;
    }

    @RabbitListener(queues = "${app.messaging.rabbitmq.estudiantes.queue}")
    public void procesarRegistro(Estudiante estudiante) {
        log.info("Mensaje recibido de la cola para registrar al estudiante: {}",
                estudiante.getCodigoEstudiantil());
        estudianteUseCase.crear(estudiante);
    }
}
