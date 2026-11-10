package com.example.registroacademico.infrastructure.adapters.out.persistence.repositories;

import com.example.registroacademico.infrastructure.adapters.out.persistence.entities.MatriculaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MatriculaJpaRepository extends JpaRepository<MatriculaEntity, Integer> {

    List<MatriculaEntity> findByEstudiante_Id(int idEstudiante);

    List<MatriculaEntity> findByCurso_Id(int idCurso);

    Optional<MatriculaEntity> findByEstudiante_IdAndCurso_Id(int idEstudiante, int idCurso);

    long countByCurso_Id(int idCurso);
}
