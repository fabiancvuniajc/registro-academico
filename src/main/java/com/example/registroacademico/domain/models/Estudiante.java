package com.example.registroacademico.domain.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Estudiante {

    private int  id;
    private String codigoEstudiantil;
    private String nombres;
    private String apellidos;
    private String email;
    private Date fechaNacimiento;
    private String estado;
    private Date fechaCreacion;
}
