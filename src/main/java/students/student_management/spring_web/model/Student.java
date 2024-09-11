package students.student_management.spring_web.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@Entity
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is mandatory")
    @Size(max = 60, message = "Name cannot exceed 60 characters")
    @Column(nullable = false, length = 60)
    private String name;

    @NotBlank(message = "Contact is mandatory")
    @Size(max = 30, message = "Contact cannot exceed 30 characters")
    @Column(nullable = false, unique = true, length = 30)
    private String contact;

    @NotNull(message = "Date of birth is mandatory")
    @Temporal(TemporalType.DATE)
    private Date dob;

    @NotBlank(message = "Gender is mandatory")
    @Size(max = 10, message = "Gender cannot exceed 10 characters")
    @Column(nullable = false, length = 10)
    private String gender;

    @NotNull(message = "Student status is mandatory")
    @ManyToOne
    @JoinColumn(name = "student_status_id", nullable = false)
    private StudentStatus studentStatus;

    @NotNull(message = "Department is mandatory")
    @ManyToOne
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;
}