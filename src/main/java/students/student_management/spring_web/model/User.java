package students.student_management.spring_web.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 30)
    @NotBlank(message = "Username cannot be blank")
    private String username;

    @Column(nullable = false)
    @NotBlank(message = "Password cannot be blank")
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    // Returns whether the user account is enabled
    @Getter
    @Column(nullable = false)
    private boolean enabled = true;

    // Check if credentials are non-expired
    @Getter
    @Column(nullable = false)
    private boolean credentialsNonExpired = true;

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    public boolean isAccountNonLocked() {
        return true;
    }

    public boolean isAccountNonExpired() {
        return true;
    }
}