package students.student_management.spring_web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import students.student_management.spring_web.model.Class;
import students.student_management.spring_web.model.Enrollment;
import students.student_management.spring_web.model.Student;

import java.util.List;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    List<Enrollment> findByStudent(Student student);

    List<Enrollment> findByCourseClass(Class courseClass);
}
