package com.example.registroacademico.application.ports.out;

import java.time.LocalDate;

public interface PeriodoAcademicoRepository {

    long contarAbiertosVencidos(LocalDate fechaCorte);
}
