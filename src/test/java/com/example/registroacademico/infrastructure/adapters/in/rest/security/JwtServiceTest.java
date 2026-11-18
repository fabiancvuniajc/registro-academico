package com.example.registroacademico.infrastructure.adapters.in.rest.security;

import com.example.registroacademico.domain.models.Usuario;
import io.jsonwebtoken.ExpiredJwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JwtServiceTest {

    private static final String SECRET = "123456789012345678901234567890123456789012345678901234567890";

    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        setField("secret", SECRET);
        setField("expirationMs", 3600000L);
    }

    private void setField(String name, Object value) {
        try {
            var field = JwtService.class.getDeclaredField(name);
            field.setAccessible(true);
            field.set(jwtService, value);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    private Usuario usuarioAdmin() {
        return Usuario.builder()
                .id(1)
                .username("admin")
                .password("hashed")
                .estado("ACTIVO")
                .rol(1)
                .build();
    }

    @Test
    void generarToken_devuelveTokenNoVacioConUsername() {
        String token = jwtService.generarToken(usuarioAdmin());

        assertThat(token).isNotBlank();
        assertThat(jwtService.extraerUsername(token)).isEqualTo("admin");
    }

    @Test
    void extraerRoles_devuelveRolDelUsuario() {
        String token = jwtService.generarToken(usuarioAdmin());

        List<String> roles = jwtService.extraerRoles(token);

        assertThat(roles).containsExactly("ROLE_ADMIN");
    }

    @Test
    void esTokenValido_conUsernameCorrectoYNoExpirado_devuelveTrue() {
        String token = jwtService.generarToken(usuarioAdmin());

        assertThat(jwtService.esTokenValido(token, "admin")).isTrue();
    }

    @Test
    void esTokenValido_conUsernameIncorrecto_devuelveFalse() {
        String token = jwtService.generarToken(usuarioAdmin());

        assertThat(jwtService.esTokenValido(token, "otro")).isFalse();
    }

    @Test
    void esTokenValido_conTokenExpirado_lanzaExpiredJwtException() {
        setField("expirationMs", -3600000L);
        String token = jwtService.generarToken(usuarioAdmin());

        assertThatThrownBy(() -> jwtService.esTokenValido(token, "admin"))
                .isInstanceOf(ExpiredJwtException.class);
    }
}
