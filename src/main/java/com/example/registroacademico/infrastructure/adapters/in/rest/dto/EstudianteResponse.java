package com.example.registroacademico.infrastructure.adapters.in.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EstudianteResponse {

    private String codigoEstudiantil;
    private String nombres;
    private String apellidos;
    private String email;
    private String fechaNacimiento;
    private String estado;
    private String fechaCreacion;
}
