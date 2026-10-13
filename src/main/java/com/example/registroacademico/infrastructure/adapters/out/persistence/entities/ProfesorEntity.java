package com.example.registroacademico.infrastructure.adapters.out.persistence.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "profesor")
public class ProfesorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_profesor")
    private int id;

    @Column(name = "codigo_profesor")
    private String codigoProfesor;

    private String nombres;
    private String apellidos;
    private String email;
    private String especialidad;
    private String estado;
}
