package com.example_inventario.Config;

import com.example_inventario.Security.JwtAuthenticationEntryPoint;
import com.example_inventario.Security.JwtAuthenticationFilter;
import com.example_inventario.Security.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity // Opcional: si usas anotaciones de seguridad en métodos
@RequiredArgsConstructor
public class SecurityConfig {   //. SecurityConfig (configuración principal de seguridad):

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private JwtAuthenticationFilter jwtAuthFilter;

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    private final CorsConfigurationSource corsConfigurationSource;

    private static void customize(HeadersConfigurer<HttpSecurity> headers) {
        headers.frameOptions(frameOptions -> frameOptions.sameOrigin());
    }


    /**
     * Configuración principal de seguridad
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // Deshabilitar CSRF (no necesario para APIs REST con JWT)
                .csrf(csrf -> csrf.disable())

                // Configurar CORS
                .cors(cors -> cors.configurationSource(corsConfigurationSource))

                // Configurar autorización de requests
                .authorizeHttpRequests(authz -> authz
                        // Rutas públicas (sin autenticación)
                        .requestMatchers("/**").permitAll()


                        // Todas las demás rutas requieren autenticación
                        .anyRequest().authenticated()
                )

                // Configurar manejo de excepciones
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                )

                // Configurar gestión de sesiones (sin estado para JWT)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // Configurar proveedor de autenticación
                .authenticationProvider(authenticationProvider())

                // Agregar filtro JWT antes del filtro de autenticación estándar
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)

                // Permitir frames para H2 Console (solo para desarrollo)
                .headers(SecurityConfig::customize);

        return http.build();
    }


    /**
     * Codificador de contraseñas
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Proveedor de autenticación
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * Administrador de autenticación
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}

