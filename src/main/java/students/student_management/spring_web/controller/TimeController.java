package students.student_management.spring_web.controller;

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
    public ResponseEntity<List<Time>> getAllTimes() {
        return ResponseEntity.ok(timeService.getAllTimes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Time> getTimeById(@PathVariable Long id) {
        return ResponseEntity.ok(timeService.getTimeById(id));
    }

    @PostMapping
    public ResponseEntity<Time> createTime(@RequestBody Time time) {
        return ResponseEntity.ok(timeService.saveTime(time));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Time> updateTime(@PathVariable Long id, @RequestBody Time time) {
        Time updatedTime = timeService.updateTime(id, time);
        return ResponseEntity.ok(updatedTime);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTime(@PathVariable Long id) {
        timeService.deleteTime(id);
        return ResponseEntity.noContent().build();
    }
}
