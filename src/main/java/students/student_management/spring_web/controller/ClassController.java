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

    @PostMapping
    public ResponseEntity<Class> createClass(@RequestBody Class classEntity) {
        Course course = courseRepository.findById(classEntity.getCourse().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));

        classEntity.setCourse(course);

        Class savedClass = classService.saveClass(classEntity);
        return ResponseEntity.ok(savedClass);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Class> updateClass(@PathVariable Long id, @RequestBody Class classDetails) {
        Class existingClass = classService.getClassById(id);
        existingClass.setName(classDetails.getName());

        Course course = courseRepository.findById(classDetails.getCourse().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));
        existingClass.setCourse(course);

        Class updatedClass = classService.saveClass(existingClass);
        return ResponseEntity.ok(updatedClass);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClass(@PathVariable Long id) {
        try {
            classService.deleteClass(id);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }
}