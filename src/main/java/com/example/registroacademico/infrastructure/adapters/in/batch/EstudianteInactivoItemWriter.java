package com.example.registroacademico.infrastructure.adapters.in.batch;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import com.example.registroacademico.application.ports.in.ReporteEstudiantesInactivosUseCase;
import com.example.registroacademico.domain.models.Estudiante;

/**
 * Adaptador de entrada ("in") del step chunk-oriented: por cada estudiante
 * del chunk, delega la logica de negocio (que hacer con un estudiante
 * inactivo) en {@link ReporteEstudiantesInactivosUseCase}.
 */
@Component
public class EstudianteInactivoItemWriter implements ItemWriter<Estudiante> {

    private final ReporteEstudiantesInactivosUseCase reporteEstudiantesInactivosUseCase;

    public EstudianteInactivoItemWriter(ReporteEstudiantesInactivosUseCase reporteEstudiantesInactivosUseCase) {
        this.reporteEstudiantesInactivosUseCase = reporteEstudiantesInactivosUseCase;
    }

    @Override
    public void write(Chunk<? extends Estudiante> chunk) {
        for (Estudiante estudiante : chunk) {
            reporteEstudiantesInactivosUseCase.reportarInactivo(estudiante);
        }
    }
}