package com.example.registroacademico.infrastructure.adapters.out.persistence.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "curso")
public class CursoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_curso")
    private int id;

    @Column(name = "codigo_curso")
    private String codigoCurso;

    private String nombre;
    private int creditos;

    @Column(name = "cupo_maximo")
    private int cupoMaximo;

    // Relación muchos-a-uno: muchos cursos pertenecen a un profesor
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_profesor", nullable = false)
    private ProfesorEntity profesor;

    // Relación muchos-a-uno: muchos cursos pertenecen a un periodo
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_periodo", nullable = false)
    private PeriodoAcademicoEntity periodo;

    // Relación uno-a-muchos: un curso tiene muchas matrículas
    @OneToMany(mappedBy = "curso", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MatriculaEntity> matriculas;
}