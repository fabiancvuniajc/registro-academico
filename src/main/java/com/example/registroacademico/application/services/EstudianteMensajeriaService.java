package com.example.registroacademico.application.services;

import com.example.registroacademico.application.ports.in.EstudianteMensajeriaUseCase;
import com.example.registroacademico.application.ports.out.EstudianteMessagePublisher;
import com.example.registroacademico.domain.models.Estudiante;
import org.springframework.stereotype.Service;

@Service
public class EstudianteMensajeriaService implements EstudianteMensajeriaUseCase {

    private final EstudianteMessagePublisher estudianteMessagePublisher;

    public EstudianteMensajeriaService(EstudianteMessagePublisher estudianteMessagePublisher) {
        this.estudianteMessagePublisher = estudianteMessagePublisher;
    }

    @Override
    public void enviarRegistro(Estudiante estudiante) {
        // aca se podrian aplicar validaciones previas al envio a la cola
        estudianteMessagePublisher.publicarRegistro(estudiante);
    }
}
