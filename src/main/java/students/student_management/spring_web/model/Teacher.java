package students.student_management.spring_web.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
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

    @Column(nullable = false, unique = true)
    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Email should be valid")
    private String email;

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

    @NotNull(message = "Department cannot be null")
    @ManyToOne
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;
}