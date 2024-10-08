package students.student_management.spring_web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import students.student_management.spring_web.dto.AuthRequest;
import students.student_management.spring_web.util.JwtUtil;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    public ResponseEntity<Map<String, Object>> login(AuthRequest authRequest) {
        Map<String, Object> response = new HashMap<>();
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
            );

            UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getUsername());

            String jwtToken = jwtUtil.generateToken(userDetails);

            response.put("status", "SUCCESS");
            response.put("message", "Login successful");
            response.put("token", jwtToken);
            return ResponseEntity.ok().body(response);

        } catch (UsernameNotFoundException e) {
            response.put("status", "FAIL");
            response.put("message", "User not found: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);

        } catch (DisabledException e) {
            response.put("status", "FAIL");
            response.put("message", "User is disabled: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);

        } catch (BadCredentialsException e) {
            response.put("status", "FAIL");
            response.put("message", "Invalid credentials: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }
}