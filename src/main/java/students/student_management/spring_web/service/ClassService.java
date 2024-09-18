package students.student_management.spring_web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import students.student_management.spring_web.exception.ResourceNotFoundException;
import students.student_management.spring_web.model.Class;
import students.student_management.spring_web.model.Course;
import students.student_management.spring_web.repository.ClassRepository;
import students.student_management.spring_web.repository.CourseRepository;

import java.util.List;

@Service
public class ClassService {

    @Autowired
    private ClassRepository classRepository;

    @Autowired
    private CourseRepository courseRepository;

    public Class saveClass(Class classEntity) {
        Course course = courseRepository.findById(classEntity.getCourse().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + classEntity.getCourse().getId()));
        classEntity.setCourse(course);
        return classRepository.save(classEntity);
    }

    public List<Class> getAllClasses() {
        return classRepository.findAll();
    }

    public Class getClassById(Long id) {
        return classRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Class not found with id: " + id));
    }

    public void deleteClass(Long id) {
        Class existingClass = classRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Class not found with id: " + id));
        classRepository.delete(existingClass);
    }
}