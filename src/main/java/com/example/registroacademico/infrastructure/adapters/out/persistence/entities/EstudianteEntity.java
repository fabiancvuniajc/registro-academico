package com.example.registroacademico.infrastructure.adapters.out.persistence.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "estudiante")
public class EstudianteEntity {

    @Id
    @GeneratedValue(strategy =
            GenerationType.IDENTITY)
    @Column(name = "id_estudiante")
    private int  id;

    @Column(name = "codigo_estudiantil")
    private String codigoEstudiantil;
    private String nombres;
    private String apellidos;
    private String email;

    @Column(name = "fecha_nacimiento")
    private Date fechaNacimiento;

    private String estado;

    @Column(name = "fecha_creacion")
    private Date fechaCreacion;
}
