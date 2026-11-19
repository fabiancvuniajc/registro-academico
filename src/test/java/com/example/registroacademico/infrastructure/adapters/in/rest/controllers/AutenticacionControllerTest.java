package com.example.registroacademico.infrastructure.adapters.in.rest.controllers;

import com.example.registroacademico.TestWebMvcConfiguration;
import com.example.registroacademico.application.ports.in.AutenticacionUseCase;
import com.example.registroacademico.infrastructure.adapters.in.rest.security.JwtAuthenticationFilter;
import com.example.registroacademico.infrastructure.adapters.in.rest.security.JwtService;
import com.example.registroacademico.infrastructure.config.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AutenticacionController.class)
@Import({SecurityConfig.class, JwtAuthenticationFilter.class, AutenticacionController.class, TestWebMvcConfiguration.class})
class AutenticacionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AutenticacionUseCase autenticacionUseCase;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private UserDetailsService userDetailsService;

    @Test
    void login_credencialesValidas_devuelve200ConToken() throws Exception {
        when(autenticacionUseCase.login("juan", "clave123"))
                .thenReturn("token-123");

        String body = """
                {
                    "username": "juan",
                    "password": "clave123"
                }
                """;

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("token-123"))
                .andExpect(jsonPath("$.tokenType").value("Bearer"));
    }

    @Test
    void login_conDatosInvalidos_devuelve400() throws Exception {
        String body = """
                {
                    "username": "",
                    "password": ""
                }
                """;

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    void registrar_conDatosValidos_devuelve201() throws Exception {
        doNothing().when(autenticacionUseCase).registrar("nuevo", "clave123", "email@email.com");

        String body = """
                {
                    "username": "nuevo",
                    "password": "clave123",
                    "email": "email@email.com"
                }
                """;

        mockMvc.perform(post("/api/v1/auth/registro")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated());

        verify(autenticacionUseCase).registrar("nuevo", "clave123", "email@email.com");
    }

    @Test
    void registrar_passwordMuyCorto_devuelve400() throws Exception {
        String body = """
                {
                    "username": "nuevo",
                    "password": "123",
                    "email": "email@email.com"
                }
                """;

        mockMvc.perform(post("/api/v1/auth/registro")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }
}