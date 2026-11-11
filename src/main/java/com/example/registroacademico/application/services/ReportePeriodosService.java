package com.example.registroacademico.application.services;

import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.example.registroacademico.application.ports.in.ReportePeriodosUseCase;
import com.example.registroacademico.application.ports.out.PeriodoAcademicoRepository;

@Service
public class ReportePeriodosService implements ReportePeriodosUseCase {

    private static final Logger log = LoggerFactory.getLogger(ReportePeriodosService.class);

    private final PeriodoAcademicoRepository periodoAcademicoRepository;

    public ReportePeriodosService(PeriodoAcademicoRepository periodoAcademicoRepository) {
        this.periodoAcademicoRepository = periodoAcademicoRepository;
    }

    @Override
    public void reportarPeriodosVencidos(LocalDate fechaCorte) {
        long cantidad = periodoAcademicoRepository.contarAbiertosVencidos(fechaCorte);
        log.info("Periodos abiertos vencidos al {}: {}", fechaCorte, cantidad);
    }
}
