package com.example.registroacademico.application.ports.in;

import com.example.registroacademico.domain.models.Estudiante;
import com.example.registroacademico.domain.models.Wrapper;
import org.springframework.data.domain.Pageable;

public interface EstudianteUseCase {

    Wrapper<Estudiante> listar(Pageable pageable);

    //pasar todos los propiedaes de estudiante indivualmente
    //Estudiante crear(String nombre, String documento, String email, int edad);

    Estudiante crear(Estudiante estudiante);

    Estudiante buscar(String documento);

    Estudiante actualizar(String documento,
                          Estudiante estudiante);

    void borrar(String documento);
}
