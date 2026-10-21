package com.example.registroacademico.infrastructure.adapters.in.rest.controllers;

import com.example.registroacademico.application.ports.in.AutenticacionUseCase;
import com.example.registroacademico.infrastructure.adapters.in.rest.dto.LoginRequest;
import com.example.registroacademico.infrastructure.adapters.in.rest.dto.RegistroRequest;
import com.example.registroacademico.infrastructure.adapters.in.rest.dto.TokenResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Autenticación", description = "Login y registro de usuarios")
public class AutenticacionController {

    private final AutenticacionUseCase autenticacionUseCase;

    public AutenticacionController(AutenticacionUseCase autenticacionUseCase) {
        this.autenticacionUseCase = autenticacionUseCase;
    }

    @PostMapping("/login")
    @Operation(summary = "Autenticar usuario y obtener JWT")
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody LoginRequest request) {
        String token = autenticacionUseCase.login(request.getUsername(), request.getPassword());
        return ResponseEntity.ok(new TokenResponse(token, "Bearer"));
    }

    @PostMapping("/registro")
    @Operation(summary = "Registrar un nuevo usuario (rol ESTUDIANTE por defecto)")
    public ResponseEntity<Void> registrar(@Valid @RequestBody RegistroRequest request) {
        autenticacionUseCase.registrar(request.getUsername(), request.getPassword());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}