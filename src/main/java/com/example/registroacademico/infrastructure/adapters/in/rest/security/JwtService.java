package com.example.registroacademico.infrastructure.adapters.in.rest.security;

import com.example.registroacademico.domain.enums.Roles;
import com.example.registroacademico.domain.models.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${app.security.jwt.secret}")
    private String secret;

    @Value("${app.security.jwt.expiration-ms}")
    private long expirationMs;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generarToken(Usuario usuario) {

        List<String> roles = new ArrayList<>();
        roles.add(Roles.buscarPorId(usuario.getRol()).name());

        return Jwts.builder()
                .subject(usuario.getUsername())
                .claim("rol", roles)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(getSigningKey())
                .compact();
    }

    public String extraerUsername(String token) {
        return extraerClaim(token, Claims::getSubject);
    }

    @SuppressWarnings("unchecked")
    public List<String> extraerRoles(String token) {
        Claims claims = extraerTodosLosClaims(token);
        return claims.get("rol", List.class);
    }

    public boolean esTokenValido(String token, String username) {
        String usernameExtraido = extraerUsername(token);
        return usernameExtraido.equals(username) && !esTokenExpirado(token);
    }

    private boolean esTokenExpirado(String token) {
        return extraerClaim(token, Claims::getExpiration).before(new Date());
    }

    private <T> T extraerClaim(String token, Function<Claims, T> resolver) {
        Claims claims = extraerTodosLosClaims(token);
        return resolver.apply(claims);
    }

    private Claims extraerTodosLosClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
