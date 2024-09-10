package students.student_management.spring_web.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @ManyToOne
    @JoinColumn(name = "time_id", nullable = false)
    private Time time;

    @ManyToOne
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;
}
