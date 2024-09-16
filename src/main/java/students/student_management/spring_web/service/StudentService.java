package students.student_management.spring_web.service;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import students.student_management.spring_web.exception.ResourceNotFoundException;
import students.student_management.spring_web.model.Department;
import students.student_management.spring_web.model.PhoneVerificationResponse;
import students.student_management.spring_web.model.Student;
import students.student_management.spring_web.model.StudentStatus;
import students.student_management.spring_web.repository.DepartmentRepository;
import students.student_management.spring_web.repository.StudentRepository;
import students.student_management.spring_web.repository.StudentStatusRepository;

import java.util.List;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final ThirdPartyApiService thirdPartyApiService;
    private final StudentStatusRepository studentStatusRepository;
    private final DepartmentRepository departmentRepository;
    public StudentService(StudentRepository studentRepository,
                          StudentStatusRepository studentStatusRepository,
                          DepartmentRepository departmentRepository,
                          ThirdPartyApiService thirdPartyApiService) {
        this.studentRepository = studentRepository;
        this.studentStatusRepository = studentStatusRepository;
        this.departmentRepository = departmentRepository;
        this.thirdPartyApiService = thirdPartyApiService;
    }

    public boolean departmentExists(Long departmentId) {
        return departmentRepository.existsById(departmentId);
    }

    // Method to check if a student status exists
    public boolean studentStatusExists(Long studentStatusId) {
        return studentStatusRepository.existsById(studentStatusId);
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Student getStudentById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));
    }

    public Student saveStudent(Student student) {
        try {
            PhoneVerificationResponse verificationResponse = thirdPartyApiService.verifyPhoneNumber(student.getContact());

            if (!verificationResponse.isValid()) {
                throw new IllegalArgumentException("Invalid phone number");
            }

            StudentStatus status = studentStatusRepository.findById(student.getStudentStatus().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("StudentStatus not found"));
            Department department = departmentRepository.findById(student.getDepartment().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Department not found"));

            // Set the full objects
            student.setStudentStatus(status);
            student.setDepartment(department);

            return studentRepository.save(student);
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("Contact number must be unique");
        }
    }

    public Student updateStudent(Long id, Student updatedStudent) {
        // Verify phone number before updating
        PhoneVerificationResponse verificationResponse = thirdPartyApiService.verifyPhoneNumber(updatedStudent.getContact());

        if (!verificationResponse.isValid()) {
            throw new IllegalArgumentException("Invalid phone number");
        }

        // Check if the department exists
        if (!departmentExists(updatedStudent.getDepartment().getId())) {
            throw new ResourceNotFoundException("Department not found with id: " + updatedStudent.getDepartment().getId());
        }

        // Check if the student status exists
        if (!studentStatusExists(updatedStudent.getStudentStatus().getId())) {
            throw new ResourceNotFoundException("Student status not found with id: " + updatedStudent.getStudentStatus().getId());
        }

        Student existingStudent = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));

        // Fetch the complete StudentStatus and Department objects
        StudentStatus status = studentStatusRepository.findById(updatedStudent.getStudentStatus().getId())
                .orElseThrow(() -> new ResourceNotFoundException("StudentStatus not found"));
        Department department = departmentRepository.findById(updatedStudent.getDepartment().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Department not found"));

        // Update the existing student's properties
        existingStudent.setName(updatedStudent.getName());
        existingStudent.setContact(updatedStudent.getContact());
        existingStudent.setDob(updatedStudent.getDob());
        existingStudent.setGender(updatedStudent.getGender());
        existingStudent.setStudentStatus(status); // Set the full object
        existingStudent.setDepartment(department); // Set the full object

        return studentRepository.save(existingStudent);
    }


    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }
}