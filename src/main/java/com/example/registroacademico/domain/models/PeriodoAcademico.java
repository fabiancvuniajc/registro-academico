package com.example.registroacademico.domain.models;

import java.time.LocalDate;

public class PeriodoAcademico {

    private int id;

    private String nombre;

    private LocalDate fechaInicio;

    private LocalDate fechaFin;

    private String estado; // ABIERTO, CERRADO
}
