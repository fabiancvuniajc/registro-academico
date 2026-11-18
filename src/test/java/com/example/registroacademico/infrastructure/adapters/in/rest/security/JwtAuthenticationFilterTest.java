package com.example.registroacademico.infrastructure.adapters.in.rest.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    void setUp() {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        SecurityContextHolder.clearContext();
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void doFilterInternal_sinHeaderAuthorization_continuaCadenaSinAutenticar() throws Exception {
        jwtAuthenticationFilter.doFilter(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
        verifyNoInteractions(jwtService);
    }

    @Test
    void doFilterInternal_conHeaderSinBearer_continuaCadenaSinAutenticar() throws Exception {
        request.addHeader("Authorization", "Basic abc123");

        jwtAuthenticationFilter.doFilter(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
        verifyNoInteractions(jwtService);
    }

    @Test
    void doFilterInternal_conTokenValido_autenticaYContinuaCadena() throws Exception {
        request.addHeader("Authorization", "Bearer token-valido");
        when(jwtService.extraerUsername("token-valido")).thenReturn("juan");
        when(jwtService.esTokenValido("token-valido", "juan")).thenReturn(true);
        when(jwtService.extraerRoles("token-valido")).thenReturn(List.of("ROLE_ESTUDIANTE"));

        jwtAuthenticationFilter.doFilter(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        var auth = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        assertThat(auth).isNotNull();
        assertThat(auth.getName()).isEqualTo("juan");
        assertThat(auth.getAuthorities()).extracting(Object::toString).containsExactly("ROLE_ESTUDIANTE");
    }

    @Test
    void doFilterInternal_conTokenInvalido_noAutenticaPeroContinuaCadena() throws Exception {
        request.addHeader("Authorization", "Bearer token-invalido");
        when(jwtService.extraerUsername("token-invalido")).thenReturn("juan");
        when(jwtService.esTokenValido("token-invalido", "juan")).thenReturn(false);

        jwtAuthenticationFilter.doFilter(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    }
}
