package com.ti.acelera.plazoletamicroservice.configuration.security;

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
    private final AntPathMatcher pathMatcher = new AntPathMatcher();
    private final Map<String, List<String>> rolePermissions;

    public JwtValidate() {
        rolePermissions = new HashMap<>();
        rolePermissions.put("ROLE_ADMIN", Arrays.asList("/restaurant/add","/dish/dish-search-budget","/restaurant/delete"));
        rolePermissions.put("ROLE_OWNER", Arrays.asList("/dish/**","/restaurant/statistics","/restaurant/category-average-price"));
        rolePermissions.put("ROLE_EMPLOYEE", Arrays.asList("/restaurant/order-list","/restaurant/assign-order","/restaurant/ready-order","/restaurant/deliver-order"));
        rolePermissions.put("ROLE_CLIENT", Arrays.asList("/restaurant/restaurant-list","/restaurant/*/menu","/restaurant/order","/restaurant/cancel-order","/restaurant/history-order","/dish/dish-search-budget"));
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

    public boolean validateRole(List<String> roles, HttpServletRequest request) {
        String currentRoute = request.getServletPath();
        Set<String> allowedPrefixes = new HashSet<>();
        for (String role : roles) {
            allowedPrefixes.addAll(rolePermissions.get(role));
        }
        for (String prefix : allowedPrefixes) {
            if (pathMatcher.match(prefix, currentRoute)) {
                return true;
            }
        }

        return false;
    }

}
