package students.student_management.spring_web.service;

import org.springframework.stereotype.Service;
import students.student_management.spring_web.model.Department;
import students.student_management.spring_web.model.Teacher;
import students.student_management.spring_web.repository.DepartmentRepository;
import students.student_management.spring_web.repository.TeacherRepository;

import java.util.List;

@Service
public class TeacherService {
    private final TeacherRepository teacherRepository;
    private final DepartmentRepository departmentRepository;

    public TeacherService(TeacherRepository teacherRepository, DepartmentRepository departmentRepository) {
        this.teacherRepository = teacherRepository;
        this.departmentRepository = departmentRepository;
    }

    public List<Teacher> getAllTeachers() {
        return teacherRepository.findAll();
    }

    public Teacher getTeacherById(Long id) {
        return teacherRepository.findById(id).orElse(null);
    }

    public Teacher saveTeacher(Teacher teacher) {
        // Fetch the Department object from the repository using the ID provided
        if (teacher.getDepartment() != null && teacher.getDepartment().getId() != null) {
            Department department = departmentRepository.findById(teacher.getDepartment().getId()).orElse(null);
            teacher.setDepartment(department); // Set the fetched Department object
        }
        return teacherRepository.save(teacher);
    }

    public void deleteTeacher(Long id) {
        teacherRepository.deleteById(id);
    }
}
