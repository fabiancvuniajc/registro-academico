package com.example.registroacademico.application.ports.out;

import com.example.registroacademico.domain.models.Estudiante;

/**
 * Puerto de salida encargado de publicar en la cola de mensajeria
 * la informacion de un estudiante pendiente de registro.
 */
public interface EstudianteMessagePublisher {

    void publicarRegistro(Estudiante estudiante);
}
