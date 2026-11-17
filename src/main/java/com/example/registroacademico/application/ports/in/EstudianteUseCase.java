package com.example.registroacademico.application.ports.in;

import com.example.registroacademico.domain.models.Estudiante;
import com.example.registroacademico.domain.models.Wrapper;

public interface EstudianteUseCase {

    Wrapper<Estudiante> listar(int page, int size, String sortBy, boolean refresh);

    //pasar todos los propiedaes de estudiante indivualmente
    //Estudiante crear(String nombre, String documento, String email, int edad);

    Estudiante crear(Estudiante estudiante);

    Estudiante buscar(String documento);

    Estudiante actualizar(String documento,
                          Estudiante estudiante);

    void borrar(String documento);
}
