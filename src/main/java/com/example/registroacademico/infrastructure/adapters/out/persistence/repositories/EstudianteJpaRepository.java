package com.example.registroacademico.infrastructure.adapters.out.persistence.repositories;


import com.example.registroacademico.infrastructure.adapters.out.persistence.entities.EstudianteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EstudianteJpaRepository extends JpaRepository<EstudianteEntity, Integer> {

    Optional<EstudianteEntity> findByCodigoEstudiantil(String codigoEstudiantil);
}
