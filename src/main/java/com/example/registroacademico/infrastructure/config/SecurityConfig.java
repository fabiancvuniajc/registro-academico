package com.example.registroacademico.infrastructure.config;

import com.example.registroacademico.infrastructure.adapters.in.rest.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UserDetailsService userDetailsService;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter,
                          UserDetailsService userDetailsService) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/auth/**").permitAll()
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()

                        // Reglas por método HTTP + rol (más preciso que solo por path)
                        .requestMatchers(HttpMethod.POST, "/api/v1/cursos/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/cursos/**").hasAnyRole("ADMIN", "PROFESOR")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/cursos/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/v1/cursos/**").hasAnyRole("ADMIN", "PROFESOR", "ESTUDIANTE")

                        .requestMatchers(HttpMethod.POST, "/api/v1/matriculas").hasAnyRole("ADMIN", "ESTUDIANTE")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/matriculas/*/nota").hasAnyRole("ADMIN", "PROFESOR")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/matriculas/*/retirar").hasAnyRole("ADMIN", "ESTUDIANTE")

                        .requestMatchers("/api/v1/estudiantes/**").hasAnyRole("ADMIN", "PROFESOR")

                        // Cualquier otro endpoint requiere estar autenticado
                        .anyRequest().authenticated()
                )
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
