package com.example.registroacademico.application.ports.in;

import java.time.LocalDate;

public interface ReportePeriodosUseCase {

    void reportarPeriodosVencidos(LocalDate fechaCorte);
}
