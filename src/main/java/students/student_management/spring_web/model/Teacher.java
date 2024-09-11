package students.student_management.spring_web.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    @NotBlank(message = "Teacher name cannot be blank")
    private String name;

    @Column(nullable = false, unique = true, length = 30)
    @NotBlank(message = "Contact cannot be blank")
    private String contact;

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    @NotNull(message = "Hire date cannot be null")
    private Date hireDate;

    @Column(nullable = false, length = 100)
    @NotBlank(message = "Subject specialization cannot be blank")
    private String subjectSpecialization;

    @Column(nullable = false)
    @NotNull(message = "Employment status cannot be null")
    private Boolean isEmployed;

    @ManyToOne
    @JoinColumn(name = "department_id", nullable = false)
    @NotNull(message = "Department cannot be null")
    private Department department;
}