package com.example.registroacademico.infrastructure.adapters.in.rest.controllers;

import com.example.registroacademico.TestWebMvcConfiguration;
import com.example.registroacademico.application.ports.in.EstudianteMensajeriaUseCase;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = EstudianteMensajeriaController.class)
@Import({SecurityConfig.class, JwtAuthenticationFilter.class, EstudianteMensajeriaController.class, TestWebMvcConfiguration.class})
class EstudianteMensajeriaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EstudianteMensajeriaUseCase estudianteMensajeriaUseCase;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private UserDetailsService userDetailsService;

    private static final String BODY = """
            {
                "codigoEstudiantil": "EST001",
                "nombres": "Juan",
                "apellidos": "Perez",
                "email": "juan@mail.com",
                "estado": "ACTIVO"
            }
            """;

    @Test
    void enviarRegistro_conRolPermitido_devuelve202() throws Exception {
        doNothing().when(estudianteMensajeriaUseCase).enviarRegistro(any());

        mockMvc.perform(post("/api/v1/estudiantes/cola")
                        .with(user("juan").roles("ESTUDIANTE"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(BODY))
                .andExpect(status().isAccepted());

        verify(estudianteMensajeriaUseCase).enviarRegistro(any());
    }

    @Test
    void enviarRegistro_sinAutenticar_devuelve403() throws Exception {
        mockMvc.perform(post("/api/v1/estudiantes/cola")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(BODY))
                .andExpect(status().isForbidden());
    }
}
