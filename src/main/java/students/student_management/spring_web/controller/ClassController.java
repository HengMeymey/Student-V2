package students.student_management.spring_web.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import students.student_management.spring_web.exception.ResourceNotFoundException;
import students.student_management.spring_web.model.Course;
import students.student_management.spring_web.repository.CourseRepository;
import students.student_management.spring_web.service.ClassService;
import students.student_management.spring_web.model.Class;

import java.util.List;

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
    public ResponseEntity<List<Class>> getAllClasses() {
        List<Class> classes = classService.getAllClasses();
        if (classes.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(classes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getClassById(@PathVariable Long id) {
        try {
            Class classEntity = classService.getClassById(id);
            return ResponseEntity.ok(classEntity);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> createClass(@Valid @RequestBody Class classEntity) {
        try {
            Course course = courseRepository.findById(classEntity.getCourse().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + classEntity.getCourse().getId()));

            classEntity.setCourse(course);
            Class savedClass = classService.saveClass(classEntity);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedClass);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateClass(@PathVariable Long id, @Valid @RequestBody Class classDetails) {
        try {
            Class existingClass = classService.getClassById(id);
            existingClass.setName(classDetails.getName());

            Course course = courseRepository.findById(classDetails.getCourse().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + classDetails.getCourse().getId()));

            existingClass.setCourse(course);
            Class updatedClass = classService.saveClass(existingClass);
            return ResponseEntity.ok(updatedClass);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to update class: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteClass(@PathVariable Long id) {
        try {
            classService.deleteClass(id);
            return ResponseEntity.ok("Class deleted successfully.");
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}