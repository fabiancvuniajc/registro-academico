package com.example.registroacademico.application.services;

import com.example.registroacademico.application.ports.in.AutenticacionUseCase;
import com.example.registroacademico.application.ports.out.UsuarioRepository;
import com.example.registroacademico.domain.enums.Roles;
import com.example.registroacademico.domain.exceptions.ReglaDeNegocioException;
import com.example.registroacademico.domain.models.Usuario;
import com.example.registroacademico.infrastructure.adapters.in.rest.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AutenticacionService implements AutenticacionUseCase {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AutenticacionService(UsuarioRepository usuarioRepository,
                                PasswordEncoder passwordEncoder,
                                AuthenticationManager authenticationManager,
                                JwtService jwtService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @Override
    public String login(String username, String password) {
        // Delega la validación de credenciales al AuthenticationManager (usa UserDetailsService + PasswordEncoder)
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password));

        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new ReglaDeNegocioException("Usuario no encontrado"));

        return jwtService.generarToken(usuario);
    }

    @Override
    public void registrar(String username, String password) {
        if (usuarioRepository.existsByUsername(username)) {
            throw new ReglaDeNegocioException("El username ya está en uso");
        }

        Usuario usuario = Usuario.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .estado("ACTIVO")
                .rol(Roles.ROLE_ESTUDIANTE.getId()).build(); // rol por defecto

        usuarioRepository.save(usuario);
    }
}
