package com.ti.acelera.plazoletamicroservice.configuration.security;

import com.ti.acelera.plazoletamicroservice.adapters.http.handlers.ITokenUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.AntPathMatcher;

import javax.crypto.SecretKey;
import java.util.List;

public class TokenUtilsImpl implements ITokenUtils {
    @Value("${jwt.secret}")
    private String secret;
    private AntPathMatcher pathMatcher = new AntPathMatcher();
    @Override
    public String getIdFromToken(String token) {
        SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes());
        Claims claims = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
        return (String) claims.get("sub");
    }
    @Override
    public List<String> getRoles(String token) {
        SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes());
        Claims claims = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
        return (List<String>) claims.get("roles");
    }
}
