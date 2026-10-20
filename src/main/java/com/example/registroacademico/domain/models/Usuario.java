package com.example.registroacademico.domain.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Usuario {

    private int id;
    private String username;
    private String password; // hash
    private String estado;
    private Integer rol; // ej: "ADMIN", "PROFESOR"
}
