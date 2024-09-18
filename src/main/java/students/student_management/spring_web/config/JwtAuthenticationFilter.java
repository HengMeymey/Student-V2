package students.student_management.spring_web.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import students.student_management.spring_web.exception.JwtAuthenticationException;
import students.student_management.spring_web.util.JwtUtil;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    private final AuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Autowired
    public JwtAuthenticationFilter(JwtUtil jwtUtil, UserDetailsService userDetailsService, AuthenticationEntryPoint jwtAuthenticationEntryPoint) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        final String authorizationHeader = request.getHeader("Authorization");
        String requestURI = request.getRequestURI();

        // Skip filter for authentication endpoints
        if (requestURI.equals("/auth/create") || requestURI.equals("/auth/login")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Proceed if the header is present and starts with "Bearer "
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String jwt = authorizationHeader.substring(7);
            try {
                // Extract username from JWT
                String username = jwtUtil.extractUsername(jwt);

                // Load user details from the database using the username
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                // Validate the token and set authentication
                if (userDetails != null && jwtUtil.validateToken(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (JwtAuthenticationException e) {
                // Use the custom exception for invalid tokens
                jwtAuthenticationEntryPoint.commence(request, response, e);
                return;
            } catch (Exception e) {
                // Handle other exceptions
                jwtAuthenticationEntryPoint.commence(request, response, new JwtAuthenticationException("Invalid or expired token"));
                return;
            }
        } else {
            // If the authorization header is missing or incorrect
            jwtAuthenticationEntryPoint.commence(request, response, new JwtAuthenticationException("Authorization header missing or malformed"));
            return;
        }

        // Proceed with the filter chain if everything is fine
        filterChain.doFilter(request, response);
    }
}