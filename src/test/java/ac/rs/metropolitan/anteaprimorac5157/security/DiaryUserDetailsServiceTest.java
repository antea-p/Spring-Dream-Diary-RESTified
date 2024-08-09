package ac.rs.metropolitan.anteaprimorac5157.security;

import ac.rs.metropolitan.anteaprimorac5157.entity.DiaryUser;
import ac.rs.metropolitan.anteaprimorac5157.repository.DiaryUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DiaryUserDetailsServiceTest {

    private DiaryUserDetailsService diaryUserDetailsService;
    private DiaryUserRepository mockRepository;

    @BeforeEach
    void setUp() {
        this.mockRepository = mock(DiaryUserRepository.class);
        this.diaryUserDetailsService = new DiaryUserDetailsService(mockRepository);
    }

    @Test
    void loadUserByUsernameForExistingUser() {
        DiaryUser expectedUser = new DiaryUser(1, "tommy", "somePasswordHash", false);
        when(mockRepository.findByUsername("tommy"))
                .thenReturn(Optional.of(expectedUser));

        assertThat(diaryUserDetailsService.loadUserByUsername("tommy"))
                .isEqualTo(new DiaryUserDetails(expectedUser));
    }

    @Test
    void loadUserByUsernameForNonexistentUser() {
        when(mockRepository.findByUsername("tommy")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> diaryUserDetailsService.loadUserByUsername("tommy"));
    }
}