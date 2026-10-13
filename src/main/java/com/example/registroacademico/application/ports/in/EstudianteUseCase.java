package com.example.registroacademico.application.ports.in;

import com.example.registroacademico.domain.models.Estudiante;

import java.util.List;

public interface EstudianteUseCase {

    List<Estudiante> listar();

    //pasar todos los propiedaes de estudiante indivualmente
    //Estudiante crear(String nombre, String documento, String email, int edad);

    Estudiante crear(Estudiante estudiante);

    Estudiante buscar(String documento);

    Estudiante actualizar(String documento,
                          Estudiante estudiante);

    void borrar(String documento);
}
