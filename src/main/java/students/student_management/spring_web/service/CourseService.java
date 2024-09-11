package students.student_management.spring_web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import students.student_management.spring_web.exception.ResourceNotFoundException;
import students.student_management.spring_web.model.Course;
import students.student_management.spring_web.model.Department;
import students.student_management.spring_web.model.Time;
import students.student_management.spring_web.repository.CourseRepository;
import students.student_management.spring_web.repository.DepartmentRepository;
import students.student_management.spring_web.repository.TimeRepository;

import java.util.List;

@Service
public class CourseService {
    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private TimeRepository timeRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    public Course saveCourse(Course course) {
        // Fetching the existing entities
        Time time = timeRepository.findById(course.getTime().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Time not found"));
        Department department = departmentRepository.findById(course.getDepartment().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Department not found"));

        // Setting the fetched entities
        course.setTime(time);
        course.setDepartment(department);

        return courseRepository.save(course);
    }

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public Course getCourseById(Long id) {
        return courseRepository.findById(id).orElse(null);
    }
    public void deleteCourse(Long id) {
        courseRepository.deleteById(id);
    }
}
