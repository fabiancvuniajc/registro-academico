package com.example.registroacademico.domain.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Curso {

    private int id;
    private String codigoCurso;
    private String nombre;
    private int creditos;
    private int cupoMaximo;
    private int idProfesor;
    private int idPeriodo;
}
