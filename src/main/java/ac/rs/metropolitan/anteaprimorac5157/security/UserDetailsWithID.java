package ac.rs.metropolitan.anteaprimorac5157.security;

import org.springframework.security.core.userdetails.UserDetails;

public interface UserDetailsWithID extends UserDetails {
    Integer getId();
}
