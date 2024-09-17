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
    public ResponseEntity<Map<String, Object>> getAllTeachers() {
        Map<String, Object> response = new HashMap<>();
        List<Teacher> teachers = teacherService.getAllTeachers();

//        if (teachers.isEmpty()) {
//            response.put("message", "No teachers found.");
//            response.put("status", "FAIL");
//            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
//        }
        if (teachers.isEmpty()) {
            response.put("message", "No teacher to display");
            response.put("status", "SUCCESS");
            response.put("data", teachers);
            return ResponseEntity.ok(response);
        }

        response.put("message", "Teachers retrieved successfully.");
        response.put("status", "SUCCESS");
        response.put("data", teachers);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getTeacherById(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Teacher teacher = teacherService.getTeacherById(id);
            response.put("message", "Teacher retrieved successfully.");
            response.put("status", "SUCCESS");
            response.put("data", teacher);
            return ResponseEntity.ok(response);
        } catch (ResourceNotFoundException e) {
            response.put("message", e.getMessage());
            response.put("status", "FAIL");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createTeacher(@Valid @RequestBody Teacher teacher) {
        Map<String, Object> response = new HashMap<>();
        try {
            Teacher savedTeacher = teacherService.saveTeacher(teacher);
            response.put("message", "Teacher created successfully!");
            response.put("status", "SUCCESS");
            response.put("data", savedTeacher);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            response.put("message", "Failed to create teacher: " + e.getMessage());
            response.put("status", "FAIL");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateTeacher(
            @PathVariable Long id,
            @Valid @RequestBody Teacher teacherDetails) {
        Map<String, Object> response = new HashMap<>();
        try {
            Teacher existingTeacher = teacherService.getTeacherById(id);
            existingTeacher.setName(teacherDetails.getName());
            existingTeacher.setEmail(teacherDetails.getEmail());
            existingTeacher.setHireDate(teacherDetails.getHireDate());
            existingTeacher.setSubjectSpecialization(teacherDetails.getSubjectSpecialization());
            existingTeacher.setIsEmployed(teacherDetails.getIsEmployed());
            existingTeacher.setDepartment(teacherDetails.getDepartment());

            Teacher updatedTeacher = teacherService.saveTeacher(existingTeacher);
            response.put("message", "Teacher updated successfully!");
            response.put("status", "SUCCESS");
            response.put("data", updatedTeacher);
            return ResponseEntity.ok(response);
        } catch (ResourceNotFoundException e) {
            response.put("message", e.getMessage());
            response.put("status", "FAIL");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            response.put("message", "Failed to update teacher: " + e.getMessage());
            response.put("status", "FAIL");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteTeacher(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            teacherService.deleteTeacher(id);
            response.put("message", "Teacher deleted successfully.");
            response.put("status", "SUCCESS");
            return ResponseEntity.ok(response);
        } catch (ResourceNotFoundException e) {
            response.put("message", e.getMessage());
            response.put("status", "FAIL");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
}