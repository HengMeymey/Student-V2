package students.student_management.spring_web.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity
public class Enrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    @NotNull(message = "Student must not be null")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "class_id", nullable = false)
    @NotNull(message = "Class must not be null")
    private Class courseClass;

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    @NotNull(message = "Enrollment date must not be null")
    private Date enrollmentDate;

    @Column(nullable = false)
    @NotNull(message = "Year must not be null")
    private Integer year;
}