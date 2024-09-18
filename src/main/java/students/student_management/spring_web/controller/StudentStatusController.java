package students.student_management.spring_web.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import students.student_management.spring_web.exception.ResourceNotFoundException;
import students.student_management.spring_web.model.StudentStatus;
import students.student_management.spring_web.service.StudentStatusService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/student-statuses")
@Validated
public class StudentStatusController {

    private final StudentStatusService studentStatusService;

    public StudentStatusController(StudentStatusService studentStatusService) {
        this.studentStatusService = studentStatusService;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllStudentStatuses() {
        List<StudentStatus> studentStatuses = studentStatusService.getAllStudentStatuses();
        Map<String, Object> response = new HashMap<>();

        if (studentStatuses.isEmpty()) {
            response.put("message", "No student status to display");
            response.put("status", "SUCCESS");
            response.put("data", studentStatuses);
            return ResponseEntity.ok(response);
        }

        response.put("message", "Student statuses retrieved successfully!");
        response.put("status", "SUCCESS");
        response.put("data", studentStatuses);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getStudentStatusById(@PathVariable Long id) {
        StudentStatus studentStatus = studentStatusService.getStudentStatusById(id);
        Map<String, Object> response = new HashMap<>();

        if (studentStatus == null) {
            throw new ResourceNotFoundException("Student Status not found with id: " + id);
        }

        response.put("message", "Student Status retrieved successfully!");
        response.put("status", "SUCCESS");
        response.put("data", studentStatus);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createStudentStatus(@Valid @RequestBody StudentStatus studentStatus) {
        try {
            StudentStatus savedStatus = studentStatusService.saveStudentStatus(studentStatus);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Student Status created successfully!");
            response.put("status", "SUCCESS");
            response.put("data", savedStatus);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "Failed to create student status: " + e.getMessage());
            errorResponse.put("status", "FAIL");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateStudentStatus(@PathVariable Long id, @Valid @RequestBody StudentStatus updatedStatus) {
        try {
            StudentStatus studentStatus = studentStatusService.updateStudentStatus(id, updatedStatus);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Student Status updated successfully!");
            response.put("status", "SUCCESS");
            response.put("data", studentStatus);

            return ResponseEntity.ok(response);
        } catch (ResourceNotFoundException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", e.getMessage());
            errorResponse.put("status", "FAIL");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "Failed to update student status: " + e.getMessage());
            errorResponse.put("status", "FAIL");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteStudentStatus(@PathVariable Long id) {
        StudentStatus studentStatus = studentStatusService.getStudentStatusById(id);

        if (studentStatus == null) {
            throw new ResourceNotFoundException("Student Status not found with id: " + id);
        }
        studentStatusService.deleteStudentStatus(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Student Status has been deleted successfully!!");
        response.put("status", "SUCCESS");

        return ResponseEntity.ok(response);
    }
}