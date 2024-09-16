package students.student_management.spring_web.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import students.student_management.spring_web.exception.ResourceNotFoundException;
import students.student_management.spring_web.model.Course;
import students.student_management.spring_web.service.CourseService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/courses")
@Validated
public class CourseController {
    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping
    public ResponseEntity<List<Course>> getAllCourses() {
        List<Course> courses = courseService.getAllCourses();
        if (courses.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCourseById(@PathVariable Long id) {
        try {
            Course course = courseService.getCourseById(id);
            return ResponseEntity.ok(course);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(createErrorResponse(e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<?> createCourse(@Valid @RequestBody Course course) {
        try {
            Course savedCourse = courseService.saveCourse(course);
            return ResponseEntity.status(HttpStatus.CREATED).body(createSuccessResponse("Course created successfully.", savedCourse));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createErrorResponse("Failed to create course: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCourse(@PathVariable Long id, @Valid @RequestBody Course courseDetails) {
        try {
            Course existingCourse = courseService.getCourseById(id);
            existingCourse.setName(courseDetails.getName());
            existingCourse.setTime(courseDetails.getTime());
            existingCourse.setDepartment(courseDetails.getDepartment());

            Course updatedCourse = courseService.saveCourse(existingCourse);
            return ResponseEntity.ok(createSuccessResponse("Course updated successfully.", updatedCourse));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(createErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createErrorResponse("Failed to update course: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCourse(@PathVariable Long id) {
        try {
            courseService.deleteCourse(id);
            return ResponseEntity.ok(createSuccessResponse("Course deleted successfully.", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(createErrorResponse(e.getMessage()));
        }
    }

    // Helper method to create success response
    private Map<String, Object> createSuccessResponse(String message, Course course) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", message);
        if (course != null) {
            response.put("course", course);
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
