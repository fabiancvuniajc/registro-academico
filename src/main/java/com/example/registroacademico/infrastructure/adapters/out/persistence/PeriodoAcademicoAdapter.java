package com.example.registroacademico.infrastructure.adapters.out.persistence;

import com.example.registroacademico.application.ports.out.PeriodoAcademicoRepository;
import com.example.registroacademico.infrastructure.adapters.out.persistence.repositories.PeriodoAcademicoJpaRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class PeriodoAcademicoAdapter implements PeriodoAcademicoRepository {

    private static final String ESTADO_ABIERTO = "ABIERTO";

    private final PeriodoAcademicoJpaRepository periodoAcademicoJpaRepository;

    public PeriodoAcademicoAdapter(PeriodoAcademicoJpaRepository periodoAcademicoJpaRepository) {
        this.periodoAcademicoJpaRepository = periodoAcademicoJpaRepository;
    }

    @Override
    public long contarAbiertosVencidos(LocalDate fechaCorte) {
        return periodoAcademicoJpaRepository.countByEstadoAndFechaFinBefore(ESTADO_ABIERTO, fechaCorte);
    }
}
