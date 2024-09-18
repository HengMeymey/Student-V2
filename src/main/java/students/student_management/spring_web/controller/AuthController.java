package students.student_management.spring_web.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import students.student_management.spring_web.dto.UserDto;
import students.student_management.spring_web.dto.AuthRequest;
import students.student_management.spring_web.service.AuthService;
import students.student_management.spring_web.service.UserService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;

    // Endpoint for user creation (registration)
    @PostMapping("/create")
    public ResponseEntity<?> createUser(@Valid @RequestBody UserDto userDto) {
        return userService.createUser(userDto);
    }

    // Endpoint for user login (authentication)
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody AuthRequest authRequest) {
        return authService.login(authRequest);
    }
}