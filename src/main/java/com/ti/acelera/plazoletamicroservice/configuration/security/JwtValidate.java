package com.ti.acelera.plazoletamicroservice.configuration.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import javax.crypto.SecretKey;
import java.util.*;

@Component
public class JwtValidate {

    @Value("${jwt.secret}")
    private String secret;
    private AntPathMatcher pathMatcher = new AntPathMatcher();
    private Map<String, List<String>> rolePermissions;

    public JwtValidate() {
        rolePermissions = new HashMap<>();
        rolePermissions.put("ROLE_ADMIN", Arrays.asList("/restaurant/add"));
        rolePermissions.put("ROLE_OWNER", Arrays.asList("/dish/**"));
    }

    public boolean validateToken(String token) {

        try {
            SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes());
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }

    }

    public List<String> getRoles(String token) {
        SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes());
        Claims claims = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
        return (List<String>) claims.get("roles");
    }

    public boolean validateRole(List<String> roles, HttpServletRequest request) {
        String currentRoute = request.getServletPath();
        Set<String> allowedPrefixes = new HashSet<>();
        for (String role : roles) {
            allowedPrefixes.addAll(rolePermissions.get(role));
        }

        for (String prefix : allowedPrefixes) {
            if (pathMatcher.matchStart(prefix, currentRoute)) {
                return true;
            }
        }

        return false;
    }

}
