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
    public ResponseEntity<Map<String, Object>> getAllCourses() {
        Map<String, Object> response = new HashMap<>();
        List<Course> courses = courseService.getAllCourses();

        if (courses.isEmpty()) {
            response.put("message", "No course to display");
            response.put("status", "SUCCESS");
            response.put("data", courses);
            return ResponseEntity.ok(response);
        }

        response.put("message", "Courses retrieved successfully.");
        response.put("status", "SUCCESS");
        response.put("data", courses);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getCourseById(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Course course = courseService.getCourseById(id);
            response.put("message", "Course retrieved successfully.");
            response.put("status", "SUCCESS");
            response.put("data", course);
            return ResponseEntity.ok(response);
        } catch (ResourceNotFoundException e) {
            response.put("message", e.getMessage());
            response.put("status", "FAIL");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createCourse(@Valid @RequestBody Course course) {
        Map<String, Object> response = new HashMap<>();
        try {
            Course savedCourse = courseService.saveCourse(course);
            response.put("message", "Course created successfully!");
            response.put("status", "SUCCESS");
            response.put("data", savedCourse);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            response.put("message", "Failed to create course: " + e.getMessage());
            response.put("status", "FAIL");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateCourse(@PathVariable Long id, @Valid @RequestBody Course courseDetails) {
        Map<String, Object> response = new HashMap<>();
        try {
            Course existingCourse = courseService.getCourseById(id);
            existingCourse.setName(courseDetails.getName());
            existingCourse.setTime(courseDetails.getTime());
            existingCourse.setDepartment(courseDetails.getDepartment());

            Course updatedCourse = courseService.saveCourse(existingCourse);
            response.put("message", "Course updated successfully!");
            response.put("status", "SUCCESS");
            response.put("data", updatedCourse);
            return ResponseEntity.ok(response);
        } catch (ResourceNotFoundException e) {
            response.put("message", e.getMessage());
            response.put("status", "FAIL");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            response.put("message", "Failed to update course: " + e.getMessage());
            response.put("status", "FAIL");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteCourse(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            courseService.deleteCourse(id);
            response.put("message", "Course deleted successfully.");
            response.put("status", "SUCCESS");
            return ResponseEntity.ok(response);
        } catch (ResourceNotFoundException e) {
            response.put("message", e.getMessage());
            response.put("status", "FAIL");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
}