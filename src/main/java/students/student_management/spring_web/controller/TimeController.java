package students.student_management.spring_web.controller;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import students.student_management.spring_web.model.Time;
import students.student_management.spring_web.service.TimeService;

import java.util.List;

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
        try {
            Time time = timeService.getTimeById(id);
            return ResponseEntity.ok(time);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Time entry not found.");
        }
    }

    @PostMapping
    public ResponseEntity<?> createTime(@Valid @RequestBody Time time) {
        try {
            Time createdTime = timeService.saveTime(time);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdTime);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to create time entry.");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTime(@PathVariable Long id, @Valid @RequestBody Time time) {
        try {
            Time updatedTime = timeService.updateTime(id, time);
            return ResponseEntity.ok(updatedTime);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to update time entry.");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTime(@PathVariable Long id) {
        try {
            timeService.deleteTime(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Time entry not found.");
        }
    }
}
