package students.student_management.spring_web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import students.student_management.spring_web.exception.ResourceNotFoundException;
import students.student_management.spring_web.model.StudentStatus;
import students.student_management.spring_web.repository.StudentStatusRepository;

import java.util.List;

@Service
public class StudentStatusService {

    private final StudentStatusRepository studentStatusRepository;

    public StudentStatusService(StudentStatusRepository studentStatusRepository) {
        this.studentStatusRepository = studentStatusRepository;
    }

    public List<StudentStatus> getAllStudentStatuses() {
        return studentStatusRepository.findAll();
    }

    public StudentStatus getStudentStatusById(Long id) {
        return studentStatusRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student status not found with id: " + id));
    }

    public StudentStatus saveStudentStatus(StudentStatus studentStatus) {
        return studentStatusRepository.save(studentStatus);
    }

    public StudentStatus updateStudentStatus(Long id, StudentStatus updatedStatus) {
        StudentStatus existingStatus = studentStatusRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student status not found with id: " + id));
        existingStatus.setName(updatedStatus.getName());
        return studentStatusRepository.save(existingStatus);
    }

    public void deleteStudentStatus(Long id) {
        StudentStatus status = studentStatusRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student status not found with id: " + id));
        studentStatusRepository.delete(status);
    }
}