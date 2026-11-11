package com.example.registroacademico.infrastructure.adapters.out.persistence.repositories;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.registroacademico.infrastructure.adapters.out.persistence.entities.PeriodoAcademicoEntity;

@Repository
public interface PeriodoAcademicoJpaRepository extends JpaRepository<PeriodoAcademicoEntity, Integer> {

    long countByEstadoAndFechaFinBefore(String estado, LocalDate fechaFin);
}
