package students.student_management.spring_web.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import students.student_management.spring_web.exception.ResourceNotFoundException;
import students.student_management.spring_web.model.Student;
import students.student_management.spring_web.repository.StudentRepository;
import students.student_management.spring_web.service.StudentService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;
    private final StudentRepository studentRepository;

    public StudentController(StudentService studentService, StudentRepository studentRepository) {
        this.studentService = studentService;
        this.studentRepository = studentRepository; // Initialize the repository
    }
    @GetMapping
    public ResponseEntity<?> getAllStudents() {
        try {
            List<Student> students = studentService.getAllStudents();
            return ResponseEntity.ok(students);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to retrieve students.");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getStudentById(@PathVariable Long id) {
        try {
            Student student = studentService.getStudentById(id);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Student retrieved successfully.");
            response.put("student", student);
            return ResponseEntity.ok(response);
        } catch (ResourceNotFoundException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "Failed to retrieve student: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createStudent(@Valid @RequestBody Student student) {
        try {
            Student createdStudent = studentService.saveStudent(student);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Student created successfully!");
            response.put("student", createdStudent);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "Failed to create student: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateStudent(
            @PathVariable Long id,
            @Valid @RequestBody Student updatedStudent) {
        try {
            Student student = studentService.updateStudent(id, updatedStudent);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Student updated successfully!");
            response.put("student", student);
            return ResponseEntity.ok(response);
        } catch (ResourceNotFoundException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "Failed to update student: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteStudent(@PathVariable Long id) {
        try {
            // Check if the student exists before attempting to delete
            if (!studentRepository.existsById(id)) {
                throw new ResourceNotFoundException("Student not found with id: " + id);
            }

            studentService.deleteStudent(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Student deleted successfully.");
            return ResponseEntity.ok(response); // 200 OK
        } catch (ResourceNotFoundException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse); // 404 Not Found
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Failed to delete student: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse); // 500 Internal Server Error
        }
    }
}