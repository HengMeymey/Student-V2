package students.student_management.spring_web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import students.student_management.spring_web.dto.StudentEnrollmentDTO;
import students.student_management.spring_web.exception.ResourceNotFoundException;
import students.student_management.spring_web.model.Course;
import students.student_management.spring_web.model.Enrollment;
import students.student_management.spring_web.model.Student;
import students.student_management.spring_web.model.Class;
import students.student_management.spring_web.repository.ClassRepository;
import students.student_management.spring_web.repository.EnrollmentRepository;
import students.student_management.spring_web.repository.StudentRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ClassRepository classRepository;

    @Autowired
    public EnrollmentService(EnrollmentRepository enrollmentRepository) {
        this.enrollmentRepository = enrollmentRepository;
    }

    public Enrollment saveEnrollment(Enrollment enrollment) {
        // Fetch the student and class by their IDs
        Student student = studentRepository.findById(enrollment.getStudent().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + enrollment.getStudent().getId()));
        Class courseClass = classRepository.findById(enrollment.getCourseClass().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Class not found with ID: " + enrollment.getCourseClass().getId()));

        // Set the fully populated student and courseClass in the enrollment
        enrollment.setStudent(student);
        enrollment.setCourseClass(courseClass);

        // Save the enrollment with populated details
        return enrollmentRepository.save(enrollment);
    }

    public List<Enrollment> getAllEnrollments() {
        return enrollmentRepository.findAll();
    }

    public Enrollment getEnrollmentById(Long id) {
        return enrollmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found with id: " + id));
    }

    public Enrollment updateEnrollment(Long id, Enrollment updatedEnrollment) {
        Enrollment existingEnrollment = enrollmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found with ID: " + id));

        if (updatedEnrollment.getStudent() != null) {
            Student student = studentRepository.findById(updatedEnrollment.getStudent().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + updatedEnrollment.getStudent().getId()));
            existingEnrollment.setStudent(student);
        }

        if (updatedEnrollment.getCourseClass() != null) {
            Class courseClass = classRepository.findById(updatedEnrollment.getCourseClass().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Class not found with ID: " + updatedEnrollment.getCourseClass().getId()));
            existingEnrollment.setCourseClass(courseClass);
        }

        existingEnrollment.setEnrollmentDate(updatedEnrollment.getEnrollmentDate());
        existingEnrollment.setYear(updatedEnrollment.getYear());

        return enrollmentRepository.save(existingEnrollment);
    }

    public void deleteEnrollment(Long id) {
        if (!enrollmentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Enrollment not found with ID: " + id);
        }
        enrollmentRepository.deleteById(id);
    }

    public List<StudentEnrollmentDTO> getAllStudentEnrollments() {
        List<Enrollment> enrollments = enrollmentRepository.findAll();

        return enrollments.stream().map(enrollment -> new StudentEnrollmentDTO(
                enrollment.getStudent().getId(),
                enrollment.getStudent().getName(),
                enrollment.getStudent().getContact(),
                enrollment.getStudent().getDob(),
                enrollment.getStudent().getGender(),
                enrollment.getStudent().getStudentStatus().getName(),
                enrollment.getStudent().getDepartment().getName(),
                enrollment.getCourseClass().getName(),
                enrollment.getCourseClass().getCourse().getName(),
                enrollment.getCourseClass().getCourse().getTime().getStartTime(),
                enrollment.getCourseClass().getCourse().getTime().getEndTime(),
                enrollment.getEnrollmentDate(),
                enrollment.getYear()
        )).collect(Collectors.toList());
    }
}