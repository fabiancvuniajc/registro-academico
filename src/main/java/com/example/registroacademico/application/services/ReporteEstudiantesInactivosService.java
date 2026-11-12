package com.example.registroacademico.application.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.example.registroacademico.application.ports.in.ReporteEstudiantesInactivosUseCase;
import com.example.registroacademico.domain.models.Estudiante;

@Service
public class ReporteEstudiantesInactivosService implements ReporteEstudiantesInactivosUseCase {

    private static final Logger log = LoggerFactory.getLogger(ReporteEstudiantesInactivosService.class);

    @Override
    public void reportarInactivo(Estudiante estudiante) {
        // Punto de extension: aqui se podria persistir el reporte,
        // enviar una notificacion, etc. Por ahora solo se registra el log.
        log.info("Estudiante inactivo (sin matricula en el periodo actual): {} - {} {}",
                estudiante.getCodigoEstudiantil(), estudiante.getNombres(), estudiante.getApellidos());
    }
}