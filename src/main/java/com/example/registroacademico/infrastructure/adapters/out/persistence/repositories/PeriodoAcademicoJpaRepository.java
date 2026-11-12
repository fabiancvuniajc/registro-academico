package com.example.registroacademico.infrastructure.adapters.out.persistence.repositories;

import com.example.registroacademico.infrastructure.adapters.out.persistence.entities.PeriodoAcademicoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface PeriodoAcademicoJpaRepository extends JpaRepository<PeriodoAcademicoEntity, Integer> {

    long countByEstadoAndFechaFinBefore(String estado, LocalDate fechaFin);
}
