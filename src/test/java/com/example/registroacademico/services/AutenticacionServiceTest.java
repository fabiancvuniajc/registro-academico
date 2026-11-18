package com.example.registroacademico.services;

import com.example.registroacademico.application.ports.out.UsuarioRepository;
import com.example.registroacademico.application.services.AutenticacionService;
import com.example.registroacademico.domain.enums.Roles;
import com.example.registroacademico.domain.exceptions.ReglaDeNegocioException;
import com.example.registroacademico.domain.models.Usuario;
import com.example.registroacademico.infrastructure.adapters.in.rest.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AutenticacionServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AutenticacionService autenticacionService;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = Usuario.builder()
                .id(1)
                .username("jperez")
                .password("hashed")
                .estado("ACTIVO")
                .rol(Roles.ROLE_ESTUDIANTE.getId())
                .build();
    }

    @Test
    void login_credencialesValidas_retornaToken() {
        when(usuarioRepository.findByUsername("jperez")).thenReturn(Optional.of(usuario));
        when(jwtService.generarToken(usuario)).thenReturn("token-generado");

        String token = autenticacionService.login("jperez", "password123");

        assertThat(token).isEqualTo("token-generado");

        ArgumentCaptor<UsernamePasswordAuthenticationToken> captor =
                ArgumentCaptor.forClass(UsernamePasswordAuthenticationToken.class);
        verify(authenticationManager).authenticate(captor.capture());
        assertThat(captor.getValue().getPrincipal()).isEqualTo("jperez");
        assertThat(captor.getValue().getCredentials()).isEqualTo("password123");

        verify(usuarioRepository).findByUsername("jperez");
        verify(jwtService).generarToken(usuario);
    }

    @Test
    void login_credencialesInvalidas_propagaExcepcionDeAutenticacion() {
        doThrow(new BadCredentialsException("Credenciales invalidas"))
                .when(authenticationManager).authenticate(any());

        assertThatThrownBy(() -> autenticacionService.login("jperez", "malapassword"))
                .isInstanceOf(BadCredentialsException.class);

        verify(usuarioRepository, never()).findByUsername(anyString());
        verify(jwtService, never()).generarToken(any());
    }

    @Test
    void login_usuarioNoEncontrado_lanzaReglaDeNegocioException() {
        when(usuarioRepository.findByUsername("desconocido")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> autenticacionService.login("desconocido", "password123"))
                .isInstanceOf(ReglaDeNegocioException.class)
                .hasMessage("Usuario no encontrado");

        verify(authenticationManager).authenticate(any());
        verify(jwtService, never()).generarToken(any());
    }

    @Test
    void registrar_usernameNoExiste_guardaUsuarioConRolPorDefecto() {
        when(usuarioRepository.existsByUsername("nuevo")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("hash-codificado");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        autenticacionService.registrar("nuevo", "password123");

        ArgumentCaptor<Usuario> captor = ArgumentCaptor.forClass(Usuario.class);
        verify(usuarioRepository).save(captor.capture());

        Usuario guardado = captor.getValue();
        assertThat(guardado.getUsername()).isEqualTo("nuevo");
        assertThat(guardado.getPassword()).isEqualTo("hash-codificado");
        assertThat(guardado.getEstado()).isEqualTo("ACTIVO");
        assertThat(guardado.getRol()).isEqualTo(Roles.ROLE_ESTUDIANTE.getId());

        verify(passwordEncoder).encode("password123");
    }

    @Test
    void registrar_usernameYaExiste_lanzaReglaDeNegocioException() {
        when(usuarioRepository.existsByUsername("jperez")).thenReturn(true);

        assertThatThrownBy(() -> autenticacionService.registrar("jperez", "password123"))
                .isInstanceOf(ReglaDeNegocioException.class)
                .hasMessage("El username ya está en uso");

        verify(usuarioRepository, never()).save(any());
        verify(passwordEncoder, never()).encode(anyString());
    }
}
