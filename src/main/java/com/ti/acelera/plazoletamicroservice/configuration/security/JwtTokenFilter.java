package com.ti.acelera.plazoletamicroservice.configuration.security;

import com.ti.acelera.plazoletamicroservice.adapters.http.handlers.ITokenUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class JwtTokenFilter extends OncePerRequestFilter {

    @Autowired
    private JwtValidate jwtValidate;
    @Autowired
    private ITokenUtils tokenUtils;
    private List<String> excludedPrefixes = Arrays.asList("/swagger-ui/**", "/v3/api-docs/**","/restaurant/verify-owner");
    private AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = getToken(request);
            if (token == null || !jwtValidate.validateToken(token)) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                return;
            }
            if (!jwtValidate.validateRole(tokenUtils.getRoles(token), request)) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Role unauthorized for this action");
                return;
            }
            String userId = tokenUtils.getIdFromToken(token);
            request.setAttribute("userId", userId);

            filterChain.doFilter(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
        }

    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String currentRoute = request.getServletPath();
        for (String prefix : excludedPrefixes) {
            if (pathMatcher.matchStart(prefix, currentRoute)) {
                return true;
            }
        }
        return false;
    }


    private String getToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7); // return everything after "Bearer "
        }
        return null;
    }

}
