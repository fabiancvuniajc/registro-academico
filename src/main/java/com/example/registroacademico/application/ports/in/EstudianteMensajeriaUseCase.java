package com.example.registroacademico.application.ports.in;

import com.example.registroacademico.domain.models.Estudiante;

/**
 * Caso de uso que permite registrar un estudiante de forma asincrona,
 * enviando la solicitud a una cola de mensajeria en lugar de persistirla
 * directamente. El registro real lo realiza el consumer correspondiente.
 */
public interface EstudianteMensajeriaUseCase {

    void enviarRegistro(Estudiante estudiante);
}