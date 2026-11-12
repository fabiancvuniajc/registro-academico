package com.example.registroacademico.application.ports.in;

import com.example.registroacademico.domain.models.Estudiante;

public interface ReporteEstudiantesInactivosUseCase {

    void reportarInactivo(Estudiante estudiante);
}