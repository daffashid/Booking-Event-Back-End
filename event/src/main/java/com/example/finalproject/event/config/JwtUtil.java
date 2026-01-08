package com.example.finalproject.event.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    /**
     * IMPORTANT:
     * - Secret HARUS statis
     * - Panjang minimal 32 karakter untuk HS256
     * - Di production, simpan di application.yml / ENV
     */
    private static final String SECRET_KEY =
            "my-super-secret-key-for-jwt-signing-123456";

    private static final long EXPIRATION_TIME =
            1000 * 60 * 60 * 24; // 24 jam

    private final Key key =
            Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));

    /* =========================
       GENERATE TOKEN
       ========================= */
    public String generateToken(String email, String role) {
        return Jwts.builder()
                .setSubject(email)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /* =========================
       VALIDATE TOKEN
       ========================= */
    public boolean validateToken(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    /* =========================
       EXTRACT DATA
       ========================= */
    public String extractEmail(String token) {
        return parseClaims(token).getSubject();
    }

    public String extractRole(String token) {
        return parseClaims(token).get("role", String.class);
    }

    /* =========================
       INTERNAL
       ========================= */
    private Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
