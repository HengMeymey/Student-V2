package students.student_management.spring_web.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import students.student_management.spring_web.exception.ResourceNotFoundException;
import students.student_management.spring_web.model.Class;
import students.student_management.spring_web.model.Course;
import students.student_management.spring_web.repository.CourseRepository;
import students.student_management.spring_web.service.ClassService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/classes")
@Validated
public class ClassController {

    private final CourseRepository courseRepository;
    private final ClassService classService;

    public ClassController(CourseRepository courseRepository, ClassService classService) {
        this.courseRepository = courseRepository;
        this.classService = classService;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllClasses() {
        Map<String, Object> response = new HashMap<>();
        List<Class> classes = classService.getAllClasses();

        if (classes.isEmpty()) {
            response.put("message", "No class to display");
            response.put("status", "SUCCESS");
            response.put("data", classes);
            return ResponseEntity.ok(response);
        }

        response.put("message", "Classes retrieved successfully.");
        response.put("status", "SUCCESS");
        response.put("data", classes);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getClassById(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Class classEntity = classService.getClassById(id);
            response.put("message", "Class retrieved successfully.");
            response.put("status", "SUCCESS");
            response.put("data", classEntity);
            return ResponseEntity.ok(response);
        } catch (ResourceNotFoundException e) {
            response.put("message", e.getMessage());
            response.put("status", "FAIL");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createClass(@Valid @RequestBody Class classEntity) {
        Map<String, Object> response = new HashMap<>();
        try {
            Course course = courseRepository.findById(classEntity.getCourse().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + classEntity.getCourse().getId()));

            classEntity.setCourse(course);
            Class savedClass = classService.saveClass(classEntity);
            response.put("message", "Class created successfully!");
            response.put("status", "SUCCESS");
            response.put("data", savedClass);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (ResourceNotFoundException e) {
            response.put("message", e.getMessage());
            response.put("status", "FAIL");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            response.put("message", "Failed to create class: " + e.getMessage());
            response.put("status", "FAIL");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateClass(@PathVariable Long id, @Valid @RequestBody Class classDetails) {
        Map<String, Object> response = new HashMap<>();
        try {
            Class existingClass = classService.getClassById(id);
            existingClass.setName(classDetails.getName());

            Course course = courseRepository.findById(classDetails.getCourse().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + classDetails.getCourse().getId()));

            existingClass.setCourse(course);
            Class updatedClass = classService.saveClass(existingClass);
            response.put("message", "Class updated successfully!");
            response.put("status", "SUCCESS");
            response.put("data", updatedClass);
            return ResponseEntity.ok(response);
        } catch (ResourceNotFoundException e) {
            response.put("message", e.getMessage());
            response.put("status", "FAIL");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            response.put("message", "Failed to update class: " + e.getMessage());
            response.put("status", "FAIL");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteClass(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            classService.deleteClass(id);
            response.put("message", "Class deleted successfully.");
            response.put("status", "SUCCESS");
            return ResponseEntity.ok(response);
        } catch (ResourceNotFoundException e) {
            response.put("message", e.getMessage());
            response.put("status", "FAIL");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
}