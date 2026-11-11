package com.example.registroacademico.infrastructure.adapters.in.batch;

import java.util.HashMap;
import java.util.Map;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import com.example.registroacademico.infrastructure.adapters.out.persistence.entities.EstudianteEntity;
import com.example.registroacademico.infrastructure.adapters.out.persistence.repositories.EstudianteJpaRepository;

/**
 * Adaptador de entrada ("in") para el step chunk-oriented: pagina los
 * estudiantes sin matricula en el periodo actual directamente desde el
 * JPA repository, sin contener logica de negocio.
 */
@Component
@StepScope
public class EstudianteInactivoItemReader extends RepositoryItemReader<EstudianteEntity> {

    public EstudianteInactivoItemReader(EstudianteJpaRepository estudianteJpaRepository) {
        setRepository(estudianteJpaRepository);
        setMethodName("findSinMatriculaEnPeriodoActual");
        setPageSize(50);

        Map<String, Sort.Direction> sorts = new HashMap<>();
        sorts.put("id", Sort.Direction.ASC);
        setSort(sorts);
    }
}
