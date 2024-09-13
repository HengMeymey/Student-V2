package students.student_management.spring_web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import students.student_management.spring_web.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);  // Use Optional<User>
    boolean existsByUsername(String username);  // Check if user exists by username
}