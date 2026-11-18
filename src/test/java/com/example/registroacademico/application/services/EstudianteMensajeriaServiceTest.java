package com.example.registroacademico.application.services;

import com.example.registroacademico.application.ports.out.EstudianteMessagePublisher;
import com.example.registroacademico.domain.models.Estudiante;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EstudianteMensajeriaServiceTest {

    @Mock
    private EstudianteMessagePublisher estudianteMessagePublisher;

    @InjectMocks
    private EstudianteMensajeriaService estudianteMensajeriaService;

    @Test
    void enviarRegistro_delegaEnElPublisher() {
        Estudiante estudiante = new Estudiante();
        estudiante.setCodigoEstudiantil("EST001");

        estudianteMensajeriaService.enviarRegistro(estudiante);

        verify(estudianteMessagePublisher).publicarRegistro(estudiante);
    }
}
