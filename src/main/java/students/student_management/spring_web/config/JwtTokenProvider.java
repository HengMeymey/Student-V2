package students.student_management.spring_web.config;

import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.GrantedAuthority;

import java.security.SignatureException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class JwtTokenProvider {

    private static final Logger logger = (Logger) LoggerFactory.getLogger(JwtTokenProvider.class);

    private String secretKey; // Your secret key for signing the token
    private long tokenValidityInMilliseconds; // Set token expiration time

    // Constructor to set secret key and token validity
    public JwtTokenProvider(String secretKey, long tokenValidityInMilliseconds) {
        this.secretKey = secretKey;
        this.tokenValidityInMilliseconds = tokenValidityInMilliseconds;
    }

    // Your methods for generating and validating JWT tokens

    public String generateToken(UserDetails userDetails) {
        // Create claims based on user details
        Claims claims = Jwts.claims().setSubject(userDetails.getUsername());
        claims.put("authorities", userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.toList()));

        long now = System.currentTimeMillis();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + tokenValidityInMilliseconds))
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (MalformedJwtException ex) {
            logger.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            logger.error("Expired JWT token");
            return false;
        } catch (UnsupportedJwtException ex) {
            logger.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            logger.error("JWT token is empty or null");
        }
        return false;
    }

    // Method to check if the token is expired based on claims
    public boolean isTokenExpired(String token) {
        Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
        Date expiration = claims.getExpiration();
        return expiration.before(new Date());
    }

    // Additional methods (optional)
    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
        return claims.getSubject();
    }

    public List<String> getAuthoritiesFromToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
        return (List<String>) claims.get("authorities");
    }
}