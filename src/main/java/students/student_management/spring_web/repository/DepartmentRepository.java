package students.student_management.spring_web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import students.student_management.spring_web.model.Department;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
}
