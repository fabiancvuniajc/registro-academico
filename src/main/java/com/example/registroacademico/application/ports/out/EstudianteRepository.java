package com.example.registroacademico.application.ports.out;

import com.example.registroacademico.domain.models.Estudiante;
import com.example.registroacademico.domain.models.Wrapper;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EstudianteRepository {

    Wrapper<Estudiante> getAll(Pageable pageable);

    //pasar todos los propiedaes de estudiante indivualmente
    //Estudiante save(String nombre, String documento, String email, int edad);

    Estudiante save(Estudiante estudiante);

    Estudiante findByDocument(String documento);

    Estudiante update(String documento, Estudiante estudiante);

    void deleteByDocument(String documento);
}
