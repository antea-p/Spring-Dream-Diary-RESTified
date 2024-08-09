package ac.rs.metropolitan.anteaprimorac5157.security;


import ac.rs.metropolitan.anteaprimorac5157.entity.DiaryUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class DiaryUserDetails implements UserDetailsWithID {

    private final DiaryUser diaryUser;

    public DiaryUserDetails(DiaryUser diaryUser) {
        this.diaryUser = diaryUser;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return diaryUser.getIsAdmin()? List.of(new SimpleGrantedAuthority("ROLE_ADMIN")) :
            List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    // Koristit će se u provjeri prava pristupa
    public Integer getId() {
        return diaryUser.getId();
    }

    @Override
    public String getPassword() {
        return diaryUser.getPassword();
    }

    @Override
    public String getUsername() {
        return diaryUser.getUsername();
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
    // DiaryUser tablica nema enabled atribut, pa su svi korisnici u ovoj aplikaciji "enabled"
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DiaryUserDetails that = (DiaryUserDetails) o;
        return Objects.equals(diaryUser, that.diaryUser);
    }

    @Override
    public int hashCode() {
        return Objects.hash(diaryUser);
    }

}
