package ac.rs.metropolitan.anteaprimorac5157.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class JWTUserDetails implements UserDetailsWithID  {
    private final String username;
    private final Integer id;
    private final Collection<? extends GrantedAuthority> authorities;

    public JWTUserDetails(String username, Integer id, Collection<? extends GrantedAuthority> authorities) {
        this.username = username;
        this.id = id;
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Integer getId() {
        return id;
    }
}
