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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import students.student_management.spring_web.model.AuthRequest;
import students.student_management.spring_web.model.JwtResponse;
import students.student_management.spring_web.model.Role;
import students.student_management.spring_web.model.User; // Import User model
import students.student_management.spring_web.repository.UserRepository; // Import User repository
import students.student_management.spring_web.util.JwtUtil;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository; // Add this to access User repository

    public ResponseEntity<?> register(User user) {
        // Check if user already exists
        if (userRepository.existsByUsername(user.getUsername())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username already exists");
        }

        // Encode password and save the user
        user.setPassword(passwordEncoder.encode(user.getPassword())); // Encode the password
        user.setRole(Role.USER); // Set the appropriate role
        user.setEnabled(true); // Ensure the user is enabled
        user.setCredentialsNonExpired(true);
        userRepository.save(user); // Save the user to the database

        return ResponseEntity.status(HttpStatus.CREATED).body("User created successfully!");
    }

    public ResponseEntity<?> login(AuthRequest authRequest) {
        try {
            // Authenticate the user
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
            );

            // Load UserDetails
            UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getUsername());

            // Generate JWT token using the UserDetails object
            String jwtToken = jwtUtil.generateToken(userDetails); // Pass UserDetails instead

            // Return the token in a JSON response
            return ResponseEntity.ok().body(new JwtResponse(jwtToken));
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found: " + e.getMessage());
        } catch (DisabledException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is disabled: " + e.getMessage());
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials: " + e.getMessage());
        }
    }

}
