package students.student_management.spring_web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import students.student_management.spring_web.model.Time;

public interface TimeRepository extends JpaRepository<Time, Long> {
}
