package dev.ticketManager.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtUtil {

    private final JwtProperties jwtProperties;

    public String generateAccessToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtProperties.getAccessExpirationMs()))
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getAccessSecret())
                .compact();
    }

    public String generateRefreshToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtProperties.getRefreshExpirationMs()))
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getRefreshSecret())
                .compact();
    }

    public String validateAccessToken(String token) {
        return Jwts.parser()
                .setSigningKey(jwtProperties.getAccessSecret())
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public String validateRefreshToken(String token) {
        return Jwts.parser()
                .setSigningKey(jwtProperties.getRefreshSecret())
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
