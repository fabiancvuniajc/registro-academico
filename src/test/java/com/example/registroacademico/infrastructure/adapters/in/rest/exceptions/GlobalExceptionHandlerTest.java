package com.example.registroacademico.infrastructure.adapters.in.rest.exceptions;

import com.example.registroacademico.domain.exceptions.RecursoNoEncontradoException;
import com.example.registroacademico.domain.exceptions.ReglaDeNegocioException;
import com.example.registroacademico.infrastructure.adapters.in.rest.dto.ApiError;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Mock
    private HttpServletRequest request;

    @BeforeEach
    void setUp() {
        when(request.getRequestURI()).thenReturn("/api/v1/estudiantes");
    }

    @Test
    void handleAuthenticationException_devuelve401() {
        AuthenticationException ex = new AuthenticationException("credenciales invalidas") {};

        ResponseEntity<ApiError> response = handler.handleAuthenticationException(ex, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(response.getBody().getError()).isEqualTo("AUTHENTICATION_ERROR");
        assertThat(response.getBody().getPath()).isEqualTo("/api/v1/estudiantes");
    }

    @Test
    void handleAccessDenied_devuelve403() {
        AccessDeniedException ex = new AccessDeniedException("sin permisos");

        ResponseEntity<ApiError> response = handler.handleAccessDenied(ex, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(response.getBody().getError()).isEqualTo("ACCESS_DENIED");
    }

    @Test
    void handleNotFound_devuelve404ConMensajeOriginal() {
        RecursoNoEncontradoException ex = new RecursoNoEncontradoException("Estudiante no encontrado");

        ResponseEntity<ApiError> response = handler.handleNotFound(ex, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody().getMessage()).isEqualTo("Estudiante no encontrado");
    }

    @Test
    void handleBusinessRule_devuelve409ConMensajeOriginal() {
        ReglaDeNegocioException ex = new ReglaDeNegocioException("Regla de negocio violada");

        ResponseEntity<ApiError> response = handler.handleBusinessRule(ex, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody().getMessage()).isEqualTo("Regla de negocio violada");
    }

    @Test
    void handleValidation_devuelve400ConDetallesDeCampos() {
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError = new FieldError("objeto", "campo", "no puede estar vacío");
        when(ex.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError));

        ResponseEntity<ApiError> response = handler.handleValidation(ex, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().getDetails()).containsExactly("campo: no puede estar vacío");
    }

    @Test
    void handleGeneric_devuelve500() {
        Exception ex = new RuntimeException("error inesperado");

        ResponseEntity<ApiError> response = handler.handleGeneric(ex, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody().getError()).isEqualTo("INTERNAL_ERROR");
    }
}
