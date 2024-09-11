package students.student_management.spring_web.model;

import jakarta.persistence.*;
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
    private Student student;

    @ManyToOne
    @JoinColumn(name = "class_id", nullable = false)
    private Class courseClass;

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date enrollmentDate;

    @Column(nullable = false)
    private Integer year;

//    public Enrollment() {
//    }
//
//    public Enrollment(Student student, Class courseClass, Date enrollmentDate, Integer year) {
//        this.student = student;
//        this.courseClass = courseClass;
//        this.enrollmentDate = enrollmentDate;
//        this.year = year;
//    }
}