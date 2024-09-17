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
    public ResponseEntity<Map<String, Object>> getAllStudents() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Student> students = studentService.getAllStudents();

            if (students.isEmpty()) {
                response.put("message", "No student to display");
                response.put("status", "SUCCESS");
                response.put("data", students);
                return ResponseEntity.ok(response);
            }

            response.put("message", "Students retrieved successfully.");
            response.put("status", "SUCCESS");
            response.put("data", students);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("message", "Failed to retrieve students.");
            response.put("status", "FAIL");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getStudentById(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Student student = studentService.getStudentById(id);
            response.put("message", "Student retrieved successfully.");
            response.put("status", "SUCCESS");
            response.put("data", student);
            return ResponseEntity.ok(response);
        } catch (ResourceNotFoundException e) {
            response.put("message", e.getMessage());
            response.put("status", "FAIL");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            response.put("message", "Failed to retrieve student: " + e.getMessage());
            response.put("status", "FAIL");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createStudent(@Valid @RequestBody Student student) {
        Map<String, Object> response = new HashMap<>();
        try {
            Student createdStudent = studentService.saveStudent(student);
            response.put("message", "Student created successfully!");
            response.put("status", "SUCCESS");
            response.put("data", createdStudent);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            response.put("message", "Failed to create student: " + e.getMessage());
            response.put("status", "FAIL");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateStudent(
            @PathVariable Long id,
            @Valid @RequestBody Student updatedStudent) {
        Map<String, Object> response = new HashMap<>();
        try {
            Student student = studentService.updateStudent(id, updatedStudent);
            response.put("message", "Student updated successfully!");
            response.put("status", "SUCCESS");
            response.put("data", student);
            return ResponseEntity.ok(response);
        } catch (ResourceNotFoundException e) {
            response.put("message", e.getMessage());
            response.put("status", "FAIL");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            response.put("message", "Failed to update student: " + e.getMessage());
            response.put("status", "FAIL");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteStudent(@PathVariable Long id) {
        Map<String, String> response = new HashMap<>();
        try {
            if (!studentRepository.existsById(id)) {
                throw new ResourceNotFoundException("Student not found with id: " + id);
            }

            studentService.deleteStudent(id);
            response.put("message", "Student deleted successfully.");
            response.put("status", "SUCCESS");
            return ResponseEntity.ok(response); // 200 OK
        } catch (ResourceNotFoundException e) {
            response.put("message", e.getMessage());
            response.put("status", "FAIL");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response); // 404 Not Found
        } catch (Exception e) {
            response.put("message", "Failed to delete student: " + e.getMessage());
            response.put("status", "FAIL");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response); // 500 Internal Server Error
        }
    }
}