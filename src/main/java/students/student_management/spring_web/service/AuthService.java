//package students.student_management.spring_web.service;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.security.authentication.DisabledException;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//import students.student_management.spring_web.model.AuthRequest;
//import students.student_management.spring_web.model.JwtResponse;
//import students.student_management.spring_web.model.Role;
//import students.student_management.spring_web.model.User; // Import User model
//import students.student_management.spring_web.repository.UserRepository; // Import User repository
//import students.student_management.spring_web.util.JwtUtil;
//
//@Service
//public class AuthService {
//
//    @Autowired
//    private AuthenticationManager authenticationManager;
//
//    @Autowired
//    private UserDetailsService userDetailsService;
//
//    @Autowired
//    private JwtUtil jwtUtil;
//
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//    @Autowired
//    private UserRepository userRepository; // Add this to access User repository
//
//    public ResponseEntity<?> login(AuthRequest authRequest) {
//        try {
//            // Authenticate the user
//            Authentication authentication = authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
//            );
//
//            // Load UserDetails
//            UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getUsername());
//
//            // Generate JWT token using the UserDetails object
//            String jwtToken = jwtUtil.generateToken(userDetails); // Pass UserDetails instead
//
//            // Return the token in a JSON response
//            return ResponseEntity.ok().body(new JwtResponse(jwtToken));
//        } catch (UsernameNotFoundException e) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found: " + e.getMessage());
//        } catch (DisabledException e) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is disabled: " + e.getMessage());
//        } catch (BadCredentialsException e) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials: " + e.getMessage());
//        }
//    }
//}

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
import students.student_management.spring_web.model.AuthRequest;
import students.student_management.spring_web.model.JwtResponse;
import students.student_management.spring_web.repository.UserRepository;
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
            // Authenticate the user
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
            );

            UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getUsername());

            // Generate JWT token
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