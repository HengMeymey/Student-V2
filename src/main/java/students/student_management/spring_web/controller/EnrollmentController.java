package students.student_management.spring_web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import students.student_management.spring_web.model.Enrollment;
import students.student_management.spring_web.model.Student;
import students.student_management.spring_web.model.Class;
import students.student_management.spring_web.service.EnrollmentService;

import java.util.List;

@RestController
@RequestMapping("/api/enrollments")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    @Autowired
    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @PostMapping
    public ResponseEntity<Enrollment> createEnrollment(@RequestBody Enrollment enrollment) {
        Enrollment savedEnrollment = enrollmentService.saveEnrollment(enrollment);
        return ResponseEntity.ok(savedEnrollment);
    }

    @GetMapping
    public ResponseEntity<List<Enrollment>> getAllEnrollments() {
        List<Enrollment> enrollments = enrollmentService.getAllEnrollments();
        return ResponseEntity.ok(enrollments);
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<Enrollment>> getEnrollmentsByStudent(@PathVariable Long studentId) {
        Student student = new Student(); // Fetch student by ID logic
        List<Enrollment> enrollments = enrollmentService.getEnrollmentsByStudent(student);
        return ResponseEntity.ok(enrollments);
    }

    @GetMapping("/class/{classId}")
    public ResponseEntity<List<Enrollment>> getEnrollmentsByCourseClass(@PathVariable Long classId) {
        Class courseClass = new Class(); // Fetch class by ID logic
        List<Enrollment> enrollments = enrollmentService.getEnrollmentsByCourseClass(courseClass);  // Updated method
        return ResponseEntity.ok(enrollments);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Enrollment> updateEnrollment(@PathVariable Long id, @RequestBody Enrollment updatedEnrollment) {
        try {
            Enrollment enrollment = enrollmentService.updateEnrollment(id, updatedEnrollment);
            return ResponseEntity.ok(enrollment);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(null); // Handle bad request error, e.g., entity not found
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEnrollment(@PathVariable Long id) {
        try {
            enrollmentService.deleteEnrollment(id);
            return ResponseEntity.noContent().build(); // Return 204 No Content on successful deletion
        } catch (RuntimeException ex) {
            return ResponseEntity.notFound().build(); // Return 404 if the enrollment is not found
        }
    }

}