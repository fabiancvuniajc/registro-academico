package com.example.registroacademico.domain.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Matricula {

    private int id;
    private int idEstudiante;
    private int idCurso;
    private Date fechaMatricula;
    private BigDecimal notaFinal;
    private String estado; // MATRICULADO, APROBADO, REPROBADO, RETIRADO
}
