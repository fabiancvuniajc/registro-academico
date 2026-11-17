package com.example.registroacademico.infrastructure.adapters.in.rest.controllers;

import com.example.registroacademico.application.ports.in.EstudianteMensajeriaUseCase;
import com.example.registroacademico.infrastructure.adapters.in.rest.dto.CreateEstudianteRequest;
import com.example.registroacademico.infrastructure.adapters.in.rest.mappers.EstudianteMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Controlador que expone un canal alterno (colas/RabbitMQ) para registrar
// estudiantes de forma asincrona, sin bloquear la respuesta HTTP al cliente.
@RestController
@RequestMapping("/api/v1/estudiantes/cola")
@Tag(name = "Estudiantes - Mensajeria", description = "Registro asincrono de estudiantes via RabbitMQ")
public class EstudianteMensajeriaController {

    private final EstudianteMensajeriaUseCase estudianteMensajeriaUseCase;

    public EstudianteMensajeriaController(EstudianteMensajeriaUseCase estudianteMensajeriaUseCase) {
        this.estudianteMensajeriaUseCase = estudianteMensajeriaUseCase;
    }

    @PostMapping
    @Operation(summary = "Envia el registro de un estudiante a la cola de mensajeria",
            description = "Publica el mensaje en RabbitMQ; el consumer realiza el registro en base de datos")
    @ApiResponse(responseCode = "202", description = "Mensaje encolado exitosamente")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROFESOR', 'ESTUDIANTE')")
    public ResponseEntity<Void> enviarRegistro(@Valid @RequestBody CreateEstudianteRequest estudiante) {
        estudianteMensajeriaUseCase.enviarRegistro(EstudianteMapper.INSTANCE.toEstudiante(estudiante));
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }
}