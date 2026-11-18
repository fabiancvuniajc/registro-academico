package com.example.registroacademico.application.services;

import com.example.registroacademico.application.ports.out.PeriodoAcademicoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReportePeriodosServiceTest {

    @Mock
    private PeriodoAcademicoRepository periodoAcademicoRepository;

    @InjectMocks
    private ReportePeriodosService reportePeriodosService;

    @Test
    void reportarPeriodosVencidos_delegaEnRepositorioYNoLanzaExcepcion() {
        LocalDate fechaCorte = LocalDate.of(2024, 1, 1);
        when(periodoAcademicoRepository.contarAbiertosVencidos(fechaCorte)).thenReturn(5L);

        reportePeriodosService.reportarPeriodosVencidos(fechaCorte);

        verify(periodoAcademicoRepository).contarAbiertosVencidos(fechaCorte);
    }
}
