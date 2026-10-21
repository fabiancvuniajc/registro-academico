package com.example.registroacademico.infrastructure.adapters.in.rest.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistroRequest {

    @NotBlank
    private String username;

    @NotBlank
    @Size(min = 6, message = "El password debe tener al menos 6 caracteres")
    private String password;
}
