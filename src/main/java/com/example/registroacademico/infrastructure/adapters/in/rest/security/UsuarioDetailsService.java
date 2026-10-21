package com.example.registroacademico.infrastructure.adapters.in.rest.security;

import com.example.registroacademico.application.ports.out.UsuarioRepository;
import com.example.registroacademico.domain.enums.Roles;
import com.example.registroacademico.domain.models.Usuario;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UsuarioDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Usuario no encontrado: " + username));

        List<String> roles = new ArrayList<>();
        roles.add(Roles.buscarPorId(usuario.getRol()).name());

        return User.builder()
                .username(usuario.getUsername())
                .password(usuario.getPassword())
                .disabled(!"ACTIVO".equals(usuario.getEstado()))
                .authorities(roles.stream()
                        .map(SimpleGrantedAuthority::new)
                        .toList())
                .build();
    }
}
