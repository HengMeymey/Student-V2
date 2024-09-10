package students.student_management.spring_web.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity
public class Teacher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 60)
    private String name;

    @Column(nullable = false, unique = true, length = 30)
    private String contact;

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date hireDate;

    @Column(nullable = false, length = 100)
    private String subjectSpecialization;

    @Column(nullable = false)
    private Boolean isEmployed;

    @ManyToOne
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;
}