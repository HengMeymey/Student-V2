package students.student_management.spring_web.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import students.student_management.spring_web.exception.ResourceNotFoundException;
import students.student_management.spring_web.model.Teacher;
import students.student_management.spring_web.service.TeacherService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/teachers")
@Validated
public class TeacherController {
    private final TeacherService teacherService;

    public TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @GetMapping
    public ResponseEntity<List<Teacher>> getAllTeachers() {
        List<Teacher> teachers = teacherService.getAllTeachers();
        if (teachers.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(teachers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTeacherById(@PathVariable Long id) {
        try {
            Teacher teacher = teacherService.getTeacherById(id);
            return ResponseEntity.ok(teacher);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(createErrorResponse(e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<?> createTeacher(@Valid @RequestBody Teacher teacher) {
        try {
            Teacher savedTeacher = teacherService.saveTeacher(teacher);
            return ResponseEntity.status(HttpStatus.CREATED).body(createSuccessResponse("Teacher created successfully!", savedTeacher));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createErrorResponse("Failed to create teacher: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTeacher(
            @PathVariable Long id,
            @Valid @RequestBody Teacher teacherDetails) {
        try {
            Teacher existingTeacher = teacherService.getTeacherById(id);
            existingTeacher.setName(teacherDetails.getName());
            existingTeacher.setEmail(teacherDetails.getEmail());
            existingTeacher.setHireDate(teacherDetails.getHireDate());
            existingTeacher.setSubjectSpecialization(teacherDetails.getSubjectSpecialization());
            existingTeacher.setIsEmployed(teacherDetails.getIsEmployed());
            existingTeacher.setDepartment(teacherDetails.getDepartment());

            Teacher updatedTeacher = teacherService.saveTeacher(existingTeacher);
            return ResponseEntity.ok(createSuccessResponse("Teacher updated successfully!", updatedTeacher));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(createErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createErrorResponse("Failed to update teacher: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTeacher(@PathVariable Long id) {
        try {
            teacherService.deleteTeacher(id);
            return ResponseEntity.ok(createSuccessResponse("Teacher deleted successfully.", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(createErrorResponse(e.getMessage()));
        }
    }

    // Helper method to create success response
    private Map<String, Object> createSuccessResponse(String message, Teacher teacher) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", message);
        if (teacher != null) {
            response.put("enrollment", teacher);
        }
        return response;
    }

    // Helper method to create error response
    private Map<String, String> createErrorResponse(String message) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("message", message);
        return errorResponse;
    }
}