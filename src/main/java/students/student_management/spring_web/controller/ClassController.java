package students.student_management.spring_web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import students.student_management.spring_web.exception.ResourceNotFoundException;
import students.student_management.spring_web.model.Course;
import students.student_management.spring_web.repository.CourseRepository;
import students.student_management.spring_web.service.ClassService;
import students.student_management.spring_web.model.Class;

import java.util.List;

@RestController
@RequestMapping("/api/classes")
public class ClassController {

    @Autowired
    private final CourseRepository courseRepository;

    private final ClassService classService;

    public ClassController(CourseRepository courseRepository, ClassService classService) {
        this.courseRepository = courseRepository;
        this.classService = classService;
    }

    @GetMapping
    public ResponseEntity<List<Class>> getAllClasses() {
        return ResponseEntity.ok(classService.getAllClasses());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Class> getClassById(@PathVariable Long id) {
        return ResponseEntity.ok(classService.getClassById(id));
    }

//    @PostMapping
//    public ResponseEntity<Class> createClass(@RequestBody Class classEntity) {
//        Class savedClass = classService.saveClass(classEntity);
//        return ResponseEntity.ok(savedClass);
//    }

    @PostMapping
    public ResponseEntity<Class> createClass(@RequestBody Class classEntity) {
        // Retrieve the existing Course entity
        Course course = courseRepository.findById(classEntity.getCourse().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));

        // Set the course property of the Class entity
        classEntity.setCourse(course);

        // Save the Class entity
        Class savedClass = classService.saveClass(classEntity);
        return ResponseEntity.ok(savedClass);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClass(@PathVariable Long id) {
        classService.deleteClass(id);
        return ResponseEntity.noContent().build();
    }
}