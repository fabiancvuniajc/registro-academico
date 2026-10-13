package com.example.registroacademico.infrastructure.adapters.in.rest.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateEstudianteRequest {

    @NotBlank(message = "El código estudiantil es obligatorio")
    @Size(max = 20, message = "El código estudiantil no puede superar 20 caracteres")
    private String codigoEstudiantil;

    @NotBlank(message = "Los nombres son obligatorios")
    private String nombres;

    @NotBlank(message = "Los apellidos son obligatorios")
    private String apellidos;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email no tiene un formato válido")
    private String email;

    @Past(message = "La fecha de nacimiento debe ser anterior a hoy")
    private LocalDate fechaNacimiento;

    @Pattern(regexp = "ACTIVO|INACTIVO", message = "Estado debe ser ACTIVO o INACTIVO")
    private String estado;
}
