package students.student_management.spring_web.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class MyUserDetails implements UserDetails {

    private final User user;

    public MyUserDetails(User user) {
        this.user = user;
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public String getPassword() {
        return user.getPassword(); // This should be the encoded password
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getAuthorities(); // Use the existing getAuthorities method from User
    }

    @Override
    public boolean isAccountNonExpired() {
        // Implement your logic here to check if the account is not expired
        // For example, you could check a database field or use a flag
        return true; // Assuming the account is not expired by default
    }
    @Override
    public boolean isAccountNonLocked() {
        // Implement your logic here to check if the account is not locked
        // For example, you could check a database field or use a flag
        return true; // Assuming the account is not locked by default
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return user.isCredentialsNonExpired(); // Ensure this returns the correct status
    }

    @Override
    public boolean isEnabled() {
        return user.isEnabled();
    }

    // Implement the remaining methods of UserDetails (not shown here)
    // These methods typically check for account non-locked, enabled status, etc.
}
