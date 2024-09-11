package students.student_management.spring_web.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import students.student_management.spring_web.exception.ResourceNotFoundException;
import students.student_management.spring_web.model.StudentStatus;
import students.student_management.spring_web.service.StudentStatusService;

import java.util.List;

@RestController
@RequestMapping("/api/student-statuses")
@Validated
public class StudentStatusController {

    private final StudentStatusService studentStatusService;

    public StudentStatusController(StudentStatusService studentStatusService) {
        this.studentStatusService = studentStatusService;
    }

    @GetMapping
    public ResponseEntity<List<StudentStatus>> getAllStudentStatuses() {
        List<StudentStatus> studentStatuses = studentStatusService.getAllStudentStatuses();
        if (studentStatuses.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(studentStatuses);
        }
        return ResponseEntity.ok(studentStatuses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getStudentStatusById(@PathVariable Long id) {
        try {
            StudentStatus studentStatus = studentStatusService.getStudentStatusById(id);
            return ResponseEntity.ok(studentStatus);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> createStudentStatus(@Valid @RequestBody StudentStatus studentStatus) {
        try {
            StudentStatus savedStatus = studentStatusService.saveStudentStatus(studentStatus);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedStatus);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to create student status: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateStudentStatus(
            @PathVariable Long id,
            @Valid @RequestBody StudentStatus updatedStatus) {
        try {
            StudentStatus studentStatus = studentStatusService.updateStudentStatus(id, updatedStatus);
            return ResponseEntity.ok(studentStatus);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to update student status: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStudentStatus(@PathVariable Long id) {
        try {
            studentStatusService.deleteStudentStatus(id);
            return ResponseEntity.status(HttpStatus.OK).body("Student status deleted successfully.");
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}