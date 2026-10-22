package com.example.registroacademico.application.ports.in;

public interface AutenticacionUseCase {

    // retorna el access token (JWT)
    String login(String username, String password);

    void registrar(String username, String password, String email);
}