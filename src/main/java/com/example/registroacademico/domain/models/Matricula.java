package com.example.registroacademico.domain.models;

import java.math.BigDecimal;
import java.util.Date;

public class Matricula {

    private int id;

    private Estudiante estudiante;

    private Curso curso;

    private Date fechaMatricula;

    private BigDecimal notaFinal;

    private String estado;
}
