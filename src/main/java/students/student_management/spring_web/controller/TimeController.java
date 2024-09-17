package students.student_management.spring_web.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import students.student_management.spring_web.exception.ResourceNotFoundException;
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
    public ResponseEntity<Map<String, Object>> getAllTimes() {
        try {
            List<Time> times = timeService.getAllTimes();
            Map<String, Object> response = new HashMap<>();

            if (times.isEmpty()) {
                response.put("message", "No time to display");
                response.put("status", "SUCCESS");
                response.put("data", times);
                return ResponseEntity.ok(response);
            }

            response.put("message", "Times retrieved successfully!");
            response.put("status", "SUCCESS");
            response.put("data", times);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "Failed to retrieve times.");
            errorResponse.put("status", "FAIL");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getTimeById(@PathVariable Long id) {
        try {
            Time time = timeService.getTimeById(id);
            if (time == null) {
                throw new ResourceNotFoundException("Time not found with id: " + id);
            }
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Time retrieved successfully!");
            response.put("status", "SUCCESS");
            response.put("data", time);

            return ResponseEntity.ok(response);
        } catch (ResourceNotFoundException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", e.getMessage());
            errorResponse.put("status", "FAIL");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createTime(@Valid @RequestBody Time time) {
        try {
            Time createdTime = timeService.saveTime(time);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Time has been created successfully!");
            response.put("status", "SUCCESS");
            response.put("data", createdTime);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "Failed to create time: " + e.getMessage());
            errorResponse.put("status", "FAIL");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateTime(@PathVariable Long id, @Valid @RequestBody Time time) {
        try {
            Time updatedTime = timeService.updateTime(id, time);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Time has been updated successfully!");
            response.put("status", "SUCCESS");
            response.put("data", updatedTime);

            return ResponseEntity.ok(response);
        } catch (ResourceNotFoundException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", e.getMessage());
            errorResponse.put("status", "FAIL");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "Failed to update time: " + e.getMessage());
            errorResponse.put("status", "FAIL");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteTime(@PathVariable Long id) {
        try {
            Time time = timeService.getTimeById(id);
            if (time == null) {
                throw new ResourceNotFoundException("Time not found with id: " + id);
            }
            timeService.deleteTime(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Time has been deleted successfully!");
            response.put("status", "SUCCESS");

            return ResponseEntity.ok(response);
        } catch (ResourceNotFoundException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", e.getMessage());
            errorResponse.put("status", "FAIL");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }
}