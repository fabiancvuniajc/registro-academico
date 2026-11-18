package com.example.registroacademico.infrastructure.adapters.in.rest.controllers;

import com.example.registroacademico.application.services.AutenticacionService;
import com.example.registroacademico.infrastructure.adapters.in.rest.dto.TokenResponse;
import com.example.registroacademico.infrastructure.adapters.in.rest.exceptions.GlobalExceptionHandler;
import com.example.registroacademico.infrastructure.adapters.in.rest.security.JwtAuthenticationFilter;
import com.example.registroacademico.infrastructure.adapters.in.rest.security.JwtService;
import com.example.registroacademico.infrastructure.adapters.in.rest.security.UsuarioDetailsService;
import com.example.registroacademico.infrastructure.adapters.out.persistence.UsuarioPersistenceAdapter;
import com.example.registroacademico.infrastructure.adapters.out.persistence.repositories.UsuarioJpaRepository;
import com.example.registroacademico.infrastructure.config.SecurityConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Prueba de integración del endpoint {@code POST /api/v1/auth/registro}.
 * <p>
 * A diferencia de {@link AutenticacionControllerTest} (slice {@code @WebMvcTest}
 * con el caso de uso mockeado), aquí se levanta el contexto real de Controller
 * → Service → Security → Repositorio JPA → Base de datos (H2 en memoria),
 * verificando el flujo completo tal como ocurriría en producción.
 * <p>
 * Se limita explícitamente el conjunto de {@code classes} del contexto (en
 * vez de usar {@code @SpringBootApplication}) para no arrastrar la
 * configuración de Redis/RabbitMQ/Batch, que requieren infraestructura
 * externa no disponible en el entorno de pruebas.
 */
@SpringBootTest(
        classes = {
                AutenticacionController.class,
                AutenticacionService.class,
                UsuarioPersistenceAdapter.class,
                UsuarioDetailsService.class,
                JwtService.class,
                JwtAuthenticationFilter.class,
                SecurityConfig.class,
                GlobalExceptionHandler.class
        },
        webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@EnableAutoConfiguration
@EnableJpaRepositories(basePackages = "com.example.registroacademico.infrastructure.adapters.out.persistence.repositories")
@EntityScan(basePackages = "com.example.registroacademico.infrastructure.adapters.out.persistence.entities")
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class AutenticacionRegistroIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UsuarioJpaRepository usuarioJpaRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void registrar_conDatosValidos_persisteUsuarioConPasswordHasheadoYRolEstudiante() throws Exception {
        String body = """
                {
                    "username": "integracion1",
                    "password": "clave123"
                }
                """;

        mockMvc.perform(post("/api/v1/auth/registro")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated());

        assertThat(usuarioJpaRepository.existsByUsername("integracion1")).isTrue();

        var entity = usuarioJpaRepository.findByUsername("integracion1").orElseThrow();
        assertThat(entity.getEstado()).isEqualTo("ACTIVO");
        assertThat(entity.getRol()).isEqualTo(3); // ROLE_ESTUDIANTE por defecto
        assertThat(entity.getPassword()).isNotEqualTo("clave123");
        assertThat(passwordEncoder.matches("clave123", entity.getPassword())).isTrue();
    }

    @Test
    void registrar_usernameYaRegistrado_devuelve409() throws Exception {
        String body = """
                {
                    "username": "duplicado",
                    "password": "clave123"
                }
                """;

        mockMvc.perform(post("/api/v1/auth/registro")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/v1/auth/registro")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("El username ya está en uso"));

        assertThat(usuarioJpaRepository.findByUsername("duplicado")).isPresent();
    }

    @Test
    void registrar_passwordMuyCorto_devuelve400YNoPersiste() throws Exception {
        String body = """
                {
                    "username": "passwordcorto",
                    "password": "123"
                }
                """;

        mockMvc.perform(post("/api/v1/auth/registro")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());

        assertThat(usuarioJpaRepository.existsByUsername("passwordcorto")).isFalse();
    }

    @Test
    void registrar_usernameVacio_devuelve400YNoPersiste() throws Exception {
        String body = """
                {
                    "username": "",
                    "password": "clave123"
                }
                """;

        mockMvc.perform(post("/api/v1/auth/registro")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());

        assertThat(usuarioJpaRepository.count()).isZero();
    }

    @Test
    void registrarYLuegoLogin_devuelveTokenJwtValido() throws Exception {
        String registroBody = """
                {
                    "username": "flujoCompleto",
                    "password": "clave123"
                }
                """;

        mockMvc.perform(post("/api/v1/auth/registro")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registroBody))
                .andExpect(status().isCreated());

        String loginBody = """
                {
                    "username": "flujoCompleto",
                    "password": "clave123"
                }
                """;

        String responseJson = mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tokenType").value("Bearer"))
                .andReturn().getResponse().getContentAsString();

        TokenResponse token = objectMapper.readValue(responseJson, TokenResponse.class);
        assertThat(token.getAccessToken()).isNotBlank();
    }
}
