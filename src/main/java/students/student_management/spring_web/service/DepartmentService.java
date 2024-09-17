package students.student_management.spring_web.service;

import org.springframework.stereotype.Service;
import students.student_management.spring_web.exception.ResourceNotFoundException;
import students.student_management.spring_web.model.Department;
import students.student_management.spring_web.model.Student;
import students.student_management.spring_web.model.Teacher;
import students.student_management.spring_web.repository.DepartmentRepository;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    public Department getDepartmentById(Long id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + id));
    }

    public Department saveDepartment(Department department) {
        return departmentRepository.save(department);
    }

    public Department updateDepartment(Long id, Department updatedDepartment) {
        Department existingDepartment = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + id));
        existingDepartment.setName(updatedDepartment.getName());
        return departmentRepository.save(existingDepartment);
    }

    public void deleteDepartment(Long id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + id));
        departmentRepository.delete(department);
    }

    public List<Student> getAllStudents() {
        return departmentRepository.findAll().stream()
                .flatMap(department -> department.getStudents().stream())
                .collect(Collectors.toList());
    }

    public List<Teacher> getAllTeachers() {
        return departmentRepository.findAll().stream()
                .flatMap(department -> department.getTeachers().stream())
                .collect(Collectors.toList());
    }

    public List<Student> getStudentsByDepartmentId(Long departmentId) {
        return departmentRepository.findById(departmentId)
                .map(Department::getStudents)
                .orElse(Collections.emptyList());
    }

    public List<Teacher> getTeachersByDepartmentId(Long departmentId) {
        return departmentRepository.findById(departmentId)
                .map(Department::getTeachers)
                .orElse(Collections.emptyList());
    }
}