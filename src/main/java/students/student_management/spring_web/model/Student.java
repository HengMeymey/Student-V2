package students.student_management.spring_web.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@Entity
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    @Size(max = 60, message = "Name cannot exceed 60 characters")
    @Column(nullable = false, length = 60)
    private String name;

    @NotNull(message = "Phone number cannot be null")
    @Column(nullable = false, unique = true)
    private String contact;

    @NotNull(message = "Date of birth is required")
    @Temporal(TemporalType.DATE)
    private Date dob;

    @NotBlank(message = "Gender is required")
    @Size(max = 10, message = "Gender cannot exceed 10 characters")
    @Column(nullable = false, length = 10)
    private String gender;

    @NotNull(message = "Student status is required")
    @ManyToOne
    @JoinColumn(name = "student_status_id", nullable = false)
    private StudentStatus studentStatus;

    @NotNull(message = "Department is required")
    @ManyToOne
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;
}