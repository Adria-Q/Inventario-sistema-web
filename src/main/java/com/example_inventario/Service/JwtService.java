package com.example_inventario.Service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    // Inyección correcta de la clave secreta desde propiedades, con valor por defecto
    @Value("${jwt.secret:ghamaniInventorySecretKey2024BootstrapSecureKey}")
    private String secretKey;

    // Tiempo de expiración en milisegundos (por defecto 24 horas)
    @Value("${jwt.expiration:86400000}")
    private long jwtExpiration;

    /**
     * Generar token JWT para un usuario
     */
    public String generateToken(UserDetails userDetails) {

        return generateToken(new HashMap<>(), userDetails);
    }

    /**
     * Generar token con claims adicionales
     */
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return buildToken(extraClaims, userDetails, jwtExpiration);
    }

    /**
     * Construir el token JWT
     */
    private String buildToken(Map<String, Object> extraClaims, UserDetails userDetails, long expiration) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())

                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Validar si el token es válido
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    /**
     * Extraer el username del token
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extraer la fecha de expiración del token
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extraer un claim específico del token
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Extraer todos los claims del token
     */
    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(getSignInKey())  // Usa verifyWith en lugar de setSigningKey
                    .build()
                    .parseSignedClaims(token)    // Usa parseSignedClaims en lugar de parseClaimsJws
                    .getPayload();
        } catch (ExpiredJwtException e) {
            throw new RuntimeException("Token expirado", e);
        } catch (UnsupportedJwtException e) {
            throw new RuntimeException("Token no soportado", e);
        } catch (MalformedJwtException e) {
            throw new RuntimeException("Token malformado", e);
        } catch (SignatureException e) {
            throw new RuntimeException("Firma del token inválida", e);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Token vacío", e);
        }
    }

    /**
     * Verificar si el token ha expirado
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Obtener la clave de firma
     */
    private SecretKey getSignInKey() {
        byte[] keyBytes = secretKey.getBytes(); // Para producción, usa Base64 decoder si es base64
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Extraer el token del header Authorization
     */
    public String extractTokenFromHeader(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }
}
