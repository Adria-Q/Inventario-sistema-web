package com.example_inventario.Security;

import com.example_inventario.Service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {  // JwtAuthenticationFilter (filtro para validar tokens en cada request):

    @Autowired
    private JwtService jwtService;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        // Rutas que no necesitan autenticación
        String requestPath = request.getServletPath();
        if (requestPath.startsWith("/api/auth/") ||
                requestPath.equals("/api/productos") && request.getMethod().equals("GET")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Obtener el header Authorization
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String username;

        // Verificar si el header existe y empieza con "Bearer "
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            // Extraer el token
            jwt = jwtService.extractTokenFromHeader(authHeader);
            username = jwtService.extractUsername(jwt);

            // Si tenemos username y no hay autenticación en el contexto
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                // Cargar los detalles del usuario
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                // Validar el token
                if (jwtService.isTokenValid(jwt, userDetails)) {

                    // Crear el token de autenticación
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );

                    // Establecer detalles adicionales
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // Establecer la autenticación en el contexto de seguridad
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (Exception e) {
            // Log del error (en producción usar logger apropiado)
            logger.error("Error al procesar JWT: " + e.getMessage());
        }

        // Continuar con la cadena de filtros
        filterChain.doFilter(request, response);
    }
}