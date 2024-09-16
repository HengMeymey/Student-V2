package students.student_management.spring_web.controller;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import students.student_management.spring_web.exception.ResourceNotFoundException;
import students.student_management.spring_web.model.Department;
import students.student_management.spring_web.model.Time;
import students.student_management.spring_web.service.TimeService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/times")
public class TimeController {
    private final TimeService timeService;

    public TimeController(TimeService timeService) {
        this.timeService = timeService;
    }

    @GetMapping
    public ResponseEntity<?> getAllTimes() {
        try {
            List<Time> times = timeService.getAllTimes();
            return ResponseEntity.ok(times);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to retrieve times.");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTimeById(@PathVariable Long id) {
        Time time = timeService.getTimeById(id);
        if (time == null) {
            throw new ResourceNotFoundException("Time not found with id: " + id);
        }
        return ResponseEntity.ok(time);
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createTime(@Valid @RequestBody Time time) {
        try {
            Time createdTime = timeService.saveTime(time);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Time has been created successfully!");
            response.put("status", createdTime);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "Failed to create time: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateTime(@PathVariable Long id, @Valid @RequestBody Time time) {
        try {
            Time updatedTime = timeService.updateTime(id, time);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Time has been updated successfully!");
            response.put("status", updatedTime);

            return ResponseEntity.ok(response);
        } catch (ResourceNotFoundException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "Failed to time: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTime(@PathVariable Long id) {
        Time time = timeService.getTimeById(id);
        if (time == null) {
            throw new ResourceNotFoundException("Time not found with id: " + id);
        }
        timeService.deleteTime(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Time has been deleted successfully !!");

        return ResponseEntity.ok(response);
    }
}
