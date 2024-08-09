package ac.rs.metropolitan.anteaprimorac5157.controller;

import ac.rs.metropolitan.anteaprimorac5157.entity.DiaryUser;
import ac.rs.metropolitan.anteaprimorac5157.entity.RegistrationCommand;
import ac.rs.metropolitan.anteaprimorac5157.exception.RegistrationFailedException;
import ac.rs.metropolitan.anteaprimorac5157.security.DiaryUserDetails;
import ac.rs.metropolitan.anteaprimorac5157.service.RegistrationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class RegistrationControllerTest {

    private RegistrationController registrationController;
    private RegistrationService mockService;
    private RegistrationCommand registrationCommand;
    private Model mockModel;
    private Authentication mockAuthentication;
    private SecurityContext mockSecurityContext;
    private HttpServletRequest mockRequest;
    private HttpSession mockSession;
    private BindingResult mockBindingResult;

    @BeforeEach
    void setUp() {
        this.mockService = mock(RegistrationService.class);
        this.registrationController = new RegistrationController(mockService);
        this.mockModel = mock(Model.class);
        this.mockAuthentication = mock(Authentication.class);
        this.mockSecurityContext = mock(SecurityContext.class);
        this.mockRequest = mock(HttpServletRequest.class);
        this.mockSession = mock(HttpSession.class);
        this.mockBindingResult = mock(BindingResult.class);
        SecurityContextHolder.clearContext();
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }


    @Test
    void testShowRegistationForm() {
        when(mockSecurityContext.getAuthentication()).thenReturn(null);
        SecurityContextHolder.setContext(mockSecurityContext);

        assertThat(registrationController.showRegister(mockModel)).isEqualTo("register");
        verify(mockModel).addAttribute("registrationCommand", new RegistrationCommand());
    }

    @Test
    void testRedirectIfAuthenticated() {
        when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
        SecurityContextHolder.setContext(mockSecurityContext);

        when(mockAuthentication.isAuthenticated()).thenReturn(true);
        when(mockAuthentication.getPrincipal()).thenReturn(
                new DiaryUser("omori", "password", false)
        );

        assertThat(registrationController.showRegister(mockModel)).isEqualTo("redirect:/diary");
    }



    @Test
    void testRegistrationSuccess() throws RegistrationFailedException {
        registrationCommand = new RegistrationCommand("sonic", "sonic-adventures-1998", "sonic-adventures-1998");
        DiaryUserDetails expectedUserDetails = new DiaryUserDetails(new DiaryUser(98, "sonic", "some-cool-hash", false));
        when(mockService.register(registrationCommand)).thenReturn(expectedUserDetails);
        when(mockRequest.getSession(true)).thenReturn(mockSession);

        assertThat(registrationController.register(mockModel, registrationCommand, mockBindingResult, mockRequest)).isEqualTo("redirect:/");
        verify(mockSession).setAttribute(eq("SPRING_SECURITY_CONTEXT"), any());
    }

    @Test
    void testRegistrationFailsIfPasswordsDontMatch() throws RegistrationFailedException {
        registrationCommand = new RegistrationCommand("sonic", "sonic-adventures-1998", "sonic-adventures-1998-whoops");
        when(mockService.register(registrationCommand)).thenThrow(new RegistrationFailedException("Passwords don't match!"));

        assertThat(registrationController.register(mockModel, registrationCommand, mockBindingResult, mockRequest)).isEqualTo("register");
        verify(mockModel).addAttribute("error", "Passwords don't match!");
    }

    @Test
    void testRegistrationFailsIfUserExists() throws RegistrationFailedException {
        registrationCommand = new RegistrationCommand("omori", "password", "password");
        when(mockService.register(registrationCommand)).thenThrow(new RegistrationFailedException("User already exists!"));

        assertThat(registrationController.register(mockModel, registrationCommand, mockBindingResult, mockRequest)).isEqualTo("register");
        verify(mockModel).addAttribute("error", "User already exists!");
    }

    @Test
    void testRegistrationFailsIfUsernameIsBlank() {
        registrationCommand = new RegistrationCommand("", "password", "password");
        when(mockBindingResult.hasErrors()).thenReturn(true);
        when(mockBindingResult.getAllErrors()).thenReturn(List.of(new ObjectError("username", "Username can't be blank!")));

        assertThat(registrationController.register(mockModel, registrationCommand, mockBindingResult, mockRequest)).isEqualTo("register");
    }

    @Test
    void testRegistrationFailsIfPasswordIsBlank() {
        registrationCommand = new RegistrationCommand("username", "", "");
        when(mockBindingResult.hasErrors()).thenReturn(true);
        when(mockBindingResult.getAllErrors()).thenReturn(List.of(new ObjectError("password", "Password can't be blank!")));

        assertThat(registrationController.register(mockModel, registrationCommand, mockBindingResult, mockRequest)).isEqualTo("register");
    }


    @Test
    void testRegistrationFailsIfUsernameTooShort() {
        registrationCommand = new RegistrationCommand("ab", "password1", "password1");
        when(mockBindingResult.hasErrors()).thenReturn(true);
        when(mockBindingResult.getAllErrors()).thenReturn(List.of(new ObjectError("username", "Username must be at least 3 characters long!")));

        assertThat(registrationController.register(mockModel, registrationCommand, mockBindingResult, mockRequest)).isEqualTo("register");
    }

    @Test
    void testRegistrationFailsIfPasswordTooShort() {
        registrationCommand = new RegistrationCommand("username", "pass", "pass");
        when(mockBindingResult.hasErrors()).thenReturn(true);
        when(mockBindingResult.getAllErrors()).thenReturn(List.of(new ObjectError("password", "Password must be at least 8 characters long!")));

        assertThat(registrationController.register(mockModel, registrationCommand, mockBindingResult, mockRequest)).isEqualTo("register");
    }

    @Test
    void testRegistrationFailsIfUsernameNotAlphanumeric() {
        registrationCommand = new RegistrationCommand("user!name", "password1", "password1");
        when(mockBindingResult.hasErrors()).thenReturn(true);
        when(mockBindingResult.getAllErrors()).thenReturn(List.of(new ObjectError("username", "Username must contain only alphanumeric characters!")));

        assertThat(registrationController.register(mockModel, registrationCommand, mockBindingResult, mockRequest)).isEqualTo("register");
    }
}