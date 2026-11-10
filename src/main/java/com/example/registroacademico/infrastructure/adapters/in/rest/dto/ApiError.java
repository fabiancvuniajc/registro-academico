package com.example.registroacademico.infrastructure.adapters.in.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiError {
    private Instant timestamp;
    private int status;
    private String error;       // ej: "NOT_FOUND", "BUSINESS_RULE_VIOLATION"
    private String message;     // mensaje general
    private String path;        // endpoint invocado
    private List<String> details; // errores de validación de campos, si aplica
}
