//package students.student_management.spring_web.service;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//import students.student_management.spring_web.dto.UserDto;
//import students.student_management.spring_web.model.Role;
//import students.student_management.spring_web.model.User;
//import students.student_management.spring_web.repository.UserRepository;
//
//@Service
//public class UserService {
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//    public ResponseEntity<?> createUser(UserDto userDto) {
//        // Check if the user already exists
//        if (userRepository.existsByUsername(userDto.getUsername())) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username already exists");
//        }
//
//        // Create a new User object from UserDto
//        User user = new User();
//        user.setUsername(userDto.getUsername());
//        user.setPassword(passwordEncoder.encode(userDto.getPassword())); // Encode the password
//        user.setRole(Role.USER); // Set role from DTO
//        user.setEnabled(true); // Ensure the user is enabled
//        user.setCredentialsNonExpired(true);
//
//        // Save the user to the database
//        userRepository.save(user);
//
//        // Return a success response
//        return ResponseEntity.status(HttpStatus.CREATED).body("User created successfully!");
//    }
//}

package students.student_management.spring_web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import students.student_management.spring_web.dto.UserDto;
import students.student_management.spring_web.model.Role;
import students.student_management.spring_web.model.User;
import students.student_management.spring_web.repository.UserRepository;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public ResponseEntity<Map<String, Object>> createUser(UserDto userDto) {
        Map<String, Object> response = new HashMap<>();

        // Check if the user already exists
        if (userRepository.existsByUsername(userDto.getUsername())) {
            response.put("status", "FAIL");
            response.put("message", "Username already exists");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        // Create a new User object from UserDto
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(passwordEncoder.encode(userDto.getPassword())); // Encode the password
        user.setRole(Role.USER); // Set default role, or from DTO if dynamic
        user.setEnabled(true); // Ensure the user is enabled
        user.setCredentialsNonExpired(true);

        // Save the user to the database
        userRepository.save(user);

        // Prepare success response
        response.put("status", "SUCCESS");
        response.put("message", "User created successfully!");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}