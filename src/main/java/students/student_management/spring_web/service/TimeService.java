package students.student_management.spring_web.service;

import org.springframework.stereotype.Service;
import students.student_management.spring_web.exception.ResourceNotFoundException;
import students.student_management.spring_web.model.Department;
import students.student_management.spring_web.model.Time;
import students.student_management.spring_web.repository.TimeRepository;

import java.util.List;

@Service
public class TimeService {
    private final TimeRepository timeRepository;

    public TimeService(TimeRepository timeRepository) {
        this.timeRepository = timeRepository;
    }

    public List<Time> getAllTimes() {
        return timeRepository.findAll();
    }

    public Time getTimeById(Long id) {
        return timeRepository.findById(id).orElse(null);
    }

    public Time saveTime(Time time) {
        return timeRepository.save(time);
    }

    public Time updateTime(Long id, Time time) {
        Time existingTime = getTimeById(id); // Fetch the existing time entity

        existingTime.setName(time.getName());
        existingTime.setStartTime(time.getStartTime());
        existingTime.setEndTime(time.getEndTime());

        return timeRepository.save(existingTime); // Save the updated entity
    }
    public void deleteTime(Long id) {
        Time time = timeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Time not found with id: " + id));
        timeRepository.delete(time);
    }
}
