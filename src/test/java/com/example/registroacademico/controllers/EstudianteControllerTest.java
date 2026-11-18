package com.example.registroacademico.controllers;

import com.example.registroacademico.TestWebMvcConfiguration;
import com.example.registroacademico.application.ports.in.EstudianteUseCase;
import com.example.registroacademico.domain.models.Estudiante;
import com.example.registroacademico.domain.models.Wrapper;
import com.example.registroacademico.infrastructure.adapters.in.rest.controllers.EstudianteController;
import com.example.registroacademico.infrastructure.adapters.in.rest.security.JwtAuthenticationFilter;
import com.example.registroacademico.infrastructure.adapters.in.rest.security.JwtService;
import com.example.registroacademico.infrastructure.config.SecurityConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = EstudianteController.class)
@Import({SecurityConfig.class, JwtAuthenticationFilter.class, EstudianteController.class, TestWebMvcConfiguration.class})
class EstudianteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EstudianteUseCase estudianteUseCase;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private UserDetailsService userDetailsService;

    private Estudiante estudianteDePrueba() {
        Estudiante estudiante = new Estudiante();
        estudiante.setId(1);
        estudiante.setCodigoEstudiantil("EST001");
        estudiante.setNombres("Juan");
        estudiante.setApellidos("Perez");
        estudiante.setEmail("juan@mail.com");
        estudiante.setEstado("ACTIVO");
        return estudiante;
    }

    @Test
    void listar_conRolAdmin_devuelve200() throws Exception {
        Wrapper<Estudiante> wrapper = new Wrapper<>();
        wrapper.setResult(List.of(estudianteDePrueba()));
        wrapper.setTotalElements(1);
        wrapper.setTotalPages(1);
        wrapper.setPage(0);
        wrapper.setSize(10);
        when(estudianteUseCase.listar(anyInt(), anyInt(), anyString(), anyBoolean()))
                .thenReturn(wrapper);

        mockMvc.perform(get("/api/v1/estudiantes")
                        .with(user("admin").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result[0].codigoEstudiantil")
                        .value("EST001"));
    }

    @Test
    void listar_sinRolAdmin_devuelve403() throws Exception {
        mockMvc.perform(get("/api/v1/estudiantes")
                        .with(user("juan").roles("ESTUDIANTE")))
                .andExpect(status().isForbidden());
    }

    @Test
    void listar_sinAutenticar_devuelve403() throws Exception {
        mockMvc.perform(get("/api/v1/estudiantes"))
                .andExpect(status().isForbidden());
    }

    @Test
    void crear_conDatosValidos_devuelve201() throws Exception {
        Estudiante estudiante = estudianteDePrueba();
        when(estudianteUseCase.crear(any(Estudiante.class))).thenReturn(estudiante);

        String body = """
                {
                    "codigoEstudiantil": "EST001",
                    "nombres": "Juan",
                    "apellidos": "Perez",
                    "email": "juan@mail.com",
                    "estado": "ACTIVO"
                }
                """;

        mockMvc.perform(post("/api/v1/estudiantes")
                        .with(user("admin").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated());
    }

    @Test
    void crear_conDatosInvalidos_devuelve400() throws Exception {
        String body = """
                {
                    "codigoEstudiantil": "",
                    "email": "no-es-email"
                }
                """;

        mockMvc.perform(post("/api/v1/estudiantes")
                        .with(user("admin").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    void buscar_estudianteExistente_devuelve200() throws Exception {
        when(estudianteUseCase.buscar("EST001")).thenReturn(estudianteDePrueba());

        mockMvc.perform(get("/api/v1/estudiantes/EST001")
                        .with(user("admin").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.codigoEstudiantil").value("EST001"));
    }

    @Test
    void borrar_delegaEnUseCaseYDevuelve204() throws Exception {
        doNothing().when(estudianteUseCase).borrar("EST001");

        mockMvc.perform(delete("/api/v1/estudiantes/EST001")
                        .with(user("admin").roles("ADMIN")))
                .andExpect(status().isNoContent());

        verify(estudianteUseCase).borrar("EST001");
    }
}