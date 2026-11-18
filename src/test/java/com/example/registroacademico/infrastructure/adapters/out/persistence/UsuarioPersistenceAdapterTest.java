package com.example.registroacademico.infrastructure.adapters.out.persistence;

import com.example.registroacademico.domain.models.Usuario;
import com.example.registroacademico.infrastructure.adapters.out.persistence.entities.UsuarioEntity;
import com.example.registroacademico.infrastructure.adapters.out.persistence.repositories.RolJpaRepository;
import com.example.registroacademico.infrastructure.adapters.out.persistence.repositories.UsuarioJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsuarioPersistenceAdapterTest {

    @Mock
    private UsuarioJpaRepository usuarioJpaRepository;

    @Mock
    private RolJpaRepository rolJpaRepository;

    private UsuarioPersistenceAdapter usuarioPersistenceAdapter;

    @BeforeEach
    void setUp() {
        usuarioPersistenceAdapter = new UsuarioPersistenceAdapter(usuarioJpaRepository, rolJpaRepository);
    }

    @Test
    void findByUsername_existente_devuelveUsuarioMapeado() {
        UsuarioEntity entity = new UsuarioEntity(1, "juan", "hashed", "ACTIVO", 3);
        when(usuarioJpaRepository.findByUsername("juan")).thenReturn(Optional.of(entity));

        Optional<Usuario> result = usuarioPersistenceAdapter.findByUsername("juan");

        assertThat(result).isPresent();
        assertThat(result.get().getUsername()).isEqualTo("juan");
        assertThat(result.get().getRol()).isEqualTo(3);
    }

    @Test
    void findByUsername_noExistente_devuelveOptionalVacio() {
        when(usuarioJpaRepository.findByUsername("fantasma")).thenReturn(Optional.empty());

        Optional<Usuario> result = usuarioPersistenceAdapter.findByUsername("fantasma");

        assertThat(result).isEmpty();
    }

    @Test
    void save_persisteYDevuelveUsuarioMapeado() {
        Usuario usuario = Usuario.builder()
                .username("juan")
                .password("hashed")
                .estado("ACTIVO")
                .rol(3)
                .build();
        UsuarioEntity saved = new UsuarioEntity(1, "juan", "hashed", "ACTIVO", 3);
        when(usuarioJpaRepository.save(any(UsuarioEntity.class))).thenReturn(saved);

        Usuario result = usuarioPersistenceAdapter.save(usuario);

        assertThat(result.getId()).isEqualTo(1);
        assertThat(result.getUsername()).isEqualTo("juan");
    }

    @Test
    void existsByUsername_delegaEnRepositorio() {
        when(usuarioJpaRepository.existsByUsername("juan")).thenReturn(true);

        boolean result = usuarioPersistenceAdapter.existsByUsername("juan");

        assertThat(result).isTrue();
    }
}
