package students.student_management.spring_web.service;

import org.springframework.stereotype.Service;
import students.student_management.spring_web.exception.ResourceNotFoundException;
import students.student_management.spring_web.model.Department;
import students.student_management.spring_web.model.Student;
import students.student_management.spring_web.model.StudentStatus;
import students.student_management.spring_web.repository.DepartmentRepository;
import students.student_management.spring_web.repository.StudentRepository;
import students.student_management.spring_web.repository.StudentStatusRepository;

import java.util.List;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final StudentStatusRepository studentStatusRepository;
    private final DepartmentRepository departmentRepository;

//    public StudentService(StudentRepository studentRepository) {
//        this.studentRepository = studentRepository;
//    }
    public StudentService(StudentRepository studentRepository,
                          StudentStatusRepository studentStatusRepository,
                          DepartmentRepository departmentRepository) {
        this.studentRepository = studentRepository;
        this.studentStatusRepository = studentStatusRepository;
        this.departmentRepository = departmentRepository;
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Student getStudentById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));
    }


    //    public Student saveStudent(Student student) {
//        return studentRepository.save(student);
//    }
public Student saveStudent(Student student) {
    // Fetch the full StudentStatus and Department objects
    StudentStatus status = studentStatusRepository.findById(student.getStudentStatus().getId())
            .orElseThrow(() -> new ResourceNotFoundException("StudentStatus not found"));
    Department department = departmentRepository.findById(student.getDepartment().getId())
            .orElseThrow(() -> new ResourceNotFoundException("Department not found"));

    // Set the full objects
    student.setStudentStatus(status);
    student.setDepartment(department);

    return studentRepository.save(student);
}

    public Student updateStudent(Long id, Student updatedStudent) {
        Student existingStudent = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));
        existingStudent.setName(updatedStudent.getName());
        existingStudent.setContact(updatedStudent.getContact());
        existingStudent.setDob(updatedStudent.getDob());
        existingStudent.setGender(updatedStudent.getGender());
        existingStudent.setStudentStatus(updatedStudent.getStudentStatus());
        existingStudent.setDepartment(updatedStudent.getDepartment());
        return studentRepository.save(existingStudent);
    }

    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }
}