package com.example.registroacademico.application.ports.out;

import com.example.registroacademico.domain.models.Estudiante;

import java.util.List;

public interface EstudianteRepository {

    List<Estudiante> getAll();

    //pasar todos los propiedaes de estudiante indivualmente
    //Estudiante save(String nombre, String documento, String email, int edad);

    Estudiante save(Estudiante estudiante);

    Estudiante findByDocument(String documento);

    Estudiante update(String documento, Estudiante estudiante);

    void deleteByDocument(String documento);
}
