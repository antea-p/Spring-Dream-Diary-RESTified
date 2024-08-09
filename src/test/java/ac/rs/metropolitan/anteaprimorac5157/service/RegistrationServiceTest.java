package ac.rs.metropolitan.anteaprimorac5157.service;

import ac.rs.metropolitan.anteaprimorac5157.entity.DiaryUser;
import ac.rs.metropolitan.anteaprimorac5157.entity.RegistrationCommand;
import ac.rs.metropolitan.anteaprimorac5157.exception.RegistrationFailedException;
import ac.rs.metropolitan.anteaprimorac5157.repository.DiaryUserRepository;
import ac.rs.metropolitan.anteaprimorac5157.security.DiaryUserDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RegistrationServiceTest {

    private RegistrationService registrationService;
    private DiaryUserRepository mockRepository;
    private PasswordEncoder mockPasswordEncoder;
    private RegistrationCommand command;

    @BeforeEach
    void setUp() {
        this.mockRepository = mock(DiaryUserRepository.class);
        this.mockPasswordEncoder = mock(PasswordEncoder.class);
        this.registrationService = new RegistrationService(mockRepository,mockPasswordEncoder);
    }

    @Test
    void testRegistrationSuccess() throws RegistrationFailedException {
        command = new RegistrationCommand("sonic", "sonic-adventures-1998", "sonic-adventures-1998");
        when(mockPasswordEncoder.encode("sonic-adventures-1998")).thenReturn("some-cool-hash");

        DiaryUser newUser = new DiaryUser(null, "sonic", "some-cool-hash", false);
        DiaryUser savedUser = new DiaryUser(98, "sonic", "some-cool-hash", false);
        DiaryUserDetails expectedUserDetails = new DiaryUserDetails(savedUser);

        when(mockRepository.findByUsername("sonic"))
                .thenReturn(Optional.empty());
        when(mockRepository.save(newUser))
                .thenReturn(savedUser);

        assertThat(registrationService.register(command)).isEqualTo(expectedUserDetails);

    }

    @Test
    void testRegistrationFailsIfPasswordsDontMatch() {
        command = new RegistrationCommand("sonic", "sonic-adventures-1998", "1-sonic-adventures-1998");
        when(mockPasswordEncoder.encode("sonic-adventures-1998")).thenReturn("some-cool-hash");

        when(mockRepository.findByUsername("sonic"))
                .thenReturn(Optional.empty());

        assertThrows(RegistrationFailedException.class,
                () -> registrationService.register(command), "Passwords don't match!"
        );
    }

    @Test
    void testRegistrationFailsIfUserExists() throws RegistrationFailedException {
        command = new RegistrationCommand("omori", "password", "password");
        when(mockPasswordEncoder.encode("password")).thenReturn("some-lame-hash");

        DiaryUser existingUser = new DiaryUser(1, "omori", "some-lame-hash", false);
        when(mockRepository.findByUsername("omori"))
                .thenReturn(Optional.of(existingUser));

        assertThrows(RegistrationFailedException.class,
                () -> registrationService.register(command), "User already exists!"
        );
    }


}