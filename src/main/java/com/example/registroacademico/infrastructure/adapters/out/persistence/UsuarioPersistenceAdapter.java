package com.example.registroacademico.infrastructure.adapters.out.persistence;

import com.example.registroacademico.application.ports.out.UsuarioRepository;
import com.example.registroacademico.domain.models.Usuario;
import com.example.registroacademico.infrastructure.adapters.out.persistence.entities.UsuarioEntity;
import com.example.registroacademico.infrastructure.adapters.out.persistence.repositories.UsuarioJpaRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UsuarioPersistenceAdapter implements UsuarioRepository {

    private final UsuarioJpaRepository usuarioJpaRepository;

    public UsuarioPersistenceAdapter(UsuarioJpaRepository usuarioJpaRepository) {
        this.usuarioJpaRepository = usuarioJpaRepository;
    }

    @Override
    public Optional<Usuario> findByUsername(String username) {
        return usuarioJpaRepository.findByUsername(username).map(this::toUsuario);
    }

    @Override
    public Usuario save(Usuario usuario) {
        UsuarioEntity entity = new UsuarioEntity();
        entity.setUsername(usuario.getUsername());
        entity.setPassword(usuario.getPassword());
        entity.setEstado(usuario.getEstado());
        entity.setRol(usuario.getRol());

        return toUsuario(usuarioJpaRepository.save(entity));
    }

    @Override
    public boolean existsByUsername(String username) {
        return usuarioJpaRepository.existsByUsername(username);
    }

    private Usuario toUsuario(UsuarioEntity entity) {
        Usuario usuario = new Usuario();
        usuario.setId(entity.getId());
        usuario.setUsername(entity.getUsername());
        usuario.setPassword(entity.getPassword());
        usuario.setEstado(entity.getEstado());
        usuario.setRol(entity.getRol());
        return usuario;
    }
}
