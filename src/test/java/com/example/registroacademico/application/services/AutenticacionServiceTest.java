package com.example.registroacademico.application.services;

import com.example.registroacademico.application.ports.out.UsuarioRepository;
import com.example.registroacademico.domain.exceptions.ReglaDeNegocioException;
import com.example.registroacademico.domain.models.Usuario;
import com.example.registroacademico.infrastructure.adapters.in.rest.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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

    @Test
    void login_credencialesValidas_devuelveToken() {
        Usuario usuario = Usuario.builder()
                .id(1)
                .username("juan")
                .password("hashed")
                .estado("ACTIVO")
                .rol(3)
                .build();

        when(usuarioRepository.findByUsername("juan"))
                .thenReturn(Optional.of(usuario));
        when(jwtService.generarToken(usuario))
                .thenReturn("token-123");

        String token = autenticacionService.login("juan", "clave123");

        assertThat(token).isEqualTo("token-123");
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtService).generarToken(usuario);
    }

    @Test
    void login_usuarioNoEncontradoTrasAutenticar_lanzaReglaDeNegocioException() {
        when(usuarioRepository.findByUsername("fantasma")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> autenticacionService.login("fantasma", "clave123"))
                .isInstanceOf(ReglaDeNegocioException.class)
                .hasMessage("Usuario no encontrado");
    }

    @Test
    void registrar_usernameDisponible_creaUsuarioConRolEstudiantePorDefecto() {
        when(usuarioRepository.existsByUsername("nuevo")).thenReturn(false);
        when(passwordEncoder.encode("clave123")).thenReturn("hashed");

        autenticacionService.registrar("nuevo", "clave123", "email@email.com");

        verify(usuarioRepository).save(argThat(usuario ->
                usuario.getUsername().equals("nuevo")
                        && usuario.getPassword().equals("hashed")
                        && usuario.getEstado().equals("ACTIVO")
                        && usuario.getRol() == 3));
    }

    @Test
    void registrar_usernameYaExiste_lanzaReglaDeNegocioException() {
        when(usuarioRepository.existsByUsername("repetido")).thenReturn(true);

        assertThatThrownBy(() -> autenticacionService.registrar("repetido", "clave123", "email@email.com"))
                .isInstanceOf(ReglaDeNegocioException.class)
                .hasMessage("El username ya está en uso");

        verify(usuarioRepository, never()).save(any());
    }
}