package students.student_management.spring_web.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Time {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name cannot exceed 100 characters")
    @Column(nullable = false, length = 100)
    private String name;

    @Min(value = 0, message = "Start time cannot be negative")
    @Max(value = 24, message = "Start time cannot exceed 24 hours")
    @Column(nullable = false)
    private float startTime;

    @Min(value = 0, message = "End time cannot be negative")
    @Max(value = 24, message = "End time cannot exceed 24 hours")
    @Column(nullable = false)
    private float endTime;
}