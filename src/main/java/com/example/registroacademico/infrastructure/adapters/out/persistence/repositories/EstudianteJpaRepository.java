package com.example.registroacademico.infrastructure.adapters.out.persistence.repositories;


import com.example.registroacademico.infrastructure.adapters.out.persistence.entities.EstudianteEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EstudianteJpaRepository extends JpaRepository<EstudianteEntity, Integer> {

    Optional<EstudianteEntity> findByCodigoEstudiantil(String codigoEstudiantil);

    @Query("""
            SELECT e FROM EstudianteEntity e
            WHERE e.id NOT IN (
                SELECT m.estudiante.id FROM MatriculaEntity m
                WHERE m.curso.periodo.estado = 'ABIERTO'
            )
            """)
    Page<EstudianteEntity> findSinMatriculaEnPeriodoActual(Pageable pageable);
}
