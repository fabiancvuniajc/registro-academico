package com.example.registroacademico.infrastructure.adapters.in.rest.security;

import com.example.registroacademico.application.ports.out.UsuarioRepository;
import com.example.registroacademico.domain.models.Usuario;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsuarioDetailsServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioDetailsService usuarioDetailsService;

    @Test
    void loadUserByUsername_usuarioActivo_devuelveUserDetailsHabilitado() {
        Usuario usuario = Usuario.builder()
                .id(1)
                .username("juan")
                .password("hashed")
                .estado("ACTIVO")
                .rol(3)
                .build();
        when(usuarioRepository.findByUsername("juan")).thenReturn(Optional.of(usuario));

        UserDetails userDetails = usuarioDetailsService.loadUserByUsername("juan");

        assertThat(userDetails.getUsername()).isEqualTo("juan");
        assertThat(userDetails.isEnabled()).isTrue();
        assertThat(userDetails.getAuthorities()).extracting(Object::toString)
                .containsExactly("ROLE_ESTUDIANTE");
    }

    @Test
    void loadUserByUsername_usuarioInactivo_devuelveUserDetailsDeshabilitado() {
        Usuario usuario = Usuario.builder()
                .id(1)
                .username("juan")
                .password("hashed")
                .estado("INACTIVO")
                .rol(2)
                .build();
        when(usuarioRepository.findByUsername("juan")).thenReturn(Optional.of(usuario));

        UserDetails userDetails = usuarioDetailsService.loadUserByUsername("juan");

        assertThat(userDetails.isEnabled()).isFalse();
    }

    @Test
    void loadUserByUsername_usuarioNoExiste_lanzaUsernameNotFoundException() {
        when(usuarioRepository.findByUsername("fantasma")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> usuarioDetailsService.loadUserByUsername("fantasma"))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessageContaining("fantasma");
    }
}
