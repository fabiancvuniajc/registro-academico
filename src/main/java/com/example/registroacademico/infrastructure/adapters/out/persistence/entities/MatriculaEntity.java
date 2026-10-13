package com.example.registroacademico.infrastructure.adapters.out.persistence.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "matricula", uniqueConstraints = {
        @UniqueConstraint(name = "uq_estudiante_curso", columnNames = {"id_estudiante", "id_curso"})
})
public class MatriculaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_matricula")
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_estudiante", nullable = false)
    private EstudianteEntity estudiante;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_curso", nullable = false)
    private CursoEntity curso;

    @Column(name = "fecha_matricula")
    private Date fechaMatricula;

    @Column(name = "nota_final")
    private BigDecimal notaFinal;

    private String estado;
}
