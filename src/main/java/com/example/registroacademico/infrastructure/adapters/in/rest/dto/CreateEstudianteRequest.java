package com.example.registroacademico.infrastructure.adapters.in.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateEstudianteRequest {

    private String codigoEstudiantil;
    private String nombres;
    private String apellidos;
    private String email;
    private Date fechaNacimiento;
    private String estado;
    private Date fechaCreacion;
}
