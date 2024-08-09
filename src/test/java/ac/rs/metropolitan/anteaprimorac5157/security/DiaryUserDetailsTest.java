package ac.rs.metropolitan.anteaprimorac5157.security;

import ac.rs.metropolitan.anteaprimorac5157.entity.DiaryUser;
import ac.rs.metropolitan.anteaprimorac5157.repository.DiaryUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class DiaryUserDetailsTest {

    private DiaryUserDetails adminDiaryUserDetails;
    private DiaryUserDetails diaryUserDetails;

    private List<SimpleGrantedAuthority> expectedRole;

    @BeforeEach
    void setUp() {
        DiaryUser adminDiaryUser = new DiaryUser(1, "sunny", "somePasswordHash", true);
        DiaryUser diaryUser = new DiaryUser(1, "omori", "somePasswordHash2", false);

        this.adminDiaryUserDetails = new DiaryUserDetails(adminDiaryUser);
        this.diaryUserDetails = new DiaryUserDetails(diaryUser);

    }

    @Test
    void testIfChoosesAdminRoleCorrectly() {
        expectedRole = List.of(new SimpleGrantedAuthority("ROLE_ADMIN"));
        assertThat(adminDiaryUserDetails.getAuthorities()).isEqualTo(expectedRole);
    }

    @Test
    void testIfChoosesUserRoleCorrectly() {
        expectedRole = List.of(new SimpleGrantedAuthority("ROLE_USER"));
        assertThat(diaryUserDetails.getAuthorities()).isEqualTo(expectedRole);
    }
}