package com.example.registroacademico.infrastructure.adapters.in.batch;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import com.example.registroacademico.domain.models.Estudiante;
import com.example.registroacademico.infrastructure.adapters.out.persistence.entities.EstudianteEntity;
import com.example.registroacademico.infrastructure.adapters.out.persistence.mappers.EstudianteMapper;

/**
 * Adaptador de entrada ("in") del step chunk-oriented: convierte la
 * entidad JPA leida por el reader al modelo de dominio, manteniendo el
 * dominio desacoplado de JPA.
 */
@Component
public class EstudianteInactivoItemProcessor implements ItemProcessor<EstudianteEntity, Estudiante> {

    @Override
    public Estudiante process(EstudianteEntity item) {
        return EstudianteMapper.INSTANCE.toEstudiante(item);
    }
}