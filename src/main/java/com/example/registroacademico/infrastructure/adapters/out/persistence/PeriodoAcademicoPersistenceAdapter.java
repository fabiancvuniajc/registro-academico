package com.example.registroacademico.infrastructure.adapters.out.persistence;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

import com.example.registroacademico.application.ports.out.PeriodoAcademicoRepository;
import com.example.registroacademico.infrastructure.adapters.out.persistence.repositories.PeriodoAcademicoJpaRepository;

@Component
public class PeriodoAcademicoPersistenceAdapter implements PeriodoAcademicoRepository {

    private static final String ESTADO_ABIERTO = "ABIERTO";

    private final PeriodoAcademicoJpaRepository periodoAcademicoJpaRepository;

    public PeriodoAcademicoPersistenceAdapter(PeriodoAcademicoJpaRepository periodoAcademicoJpaRepository) {
        this.periodoAcademicoJpaRepository = periodoAcademicoJpaRepository;
    }

    @Override
    public long contarAbiertosVencidos(LocalDate fechaCorte) {
        return periodoAcademicoJpaRepository.countByEstadoAndFechaFinBefore(ESTADO_ABIERTO, fechaCorte);
    }
}
