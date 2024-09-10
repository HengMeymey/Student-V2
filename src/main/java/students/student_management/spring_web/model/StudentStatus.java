package students.student_management.spring_web.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
//@Entity
//@Table(name = "student_status")
//public class StudentStatus {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Column(nullable = false, length = 20)
//    private String name;
//
//    @OneToMany(mappedBy = "studentStatus")
//    @JsonBackReference  // Prevent infinite recursion
//    private List<Student> students;
//
//}
@Entity
public class StudentStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    private String name;
}
