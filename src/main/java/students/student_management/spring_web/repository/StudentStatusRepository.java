package students.student_management.spring_web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import students.student_management.spring_web.model.StudentStatus;

public interface StudentStatusRepository extends JpaRepository<StudentStatus, Long> {
}
