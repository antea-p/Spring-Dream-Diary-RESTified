package ac.rs.metropolitan.anteaprimorac5157.controller;

import ac.rs.metropolitan.anteaprimorac5157.entity.DiaryUser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class LoginControllerTest {

    private LoginController loginController;
    Authentication mockAuthentication;
    SecurityContext mockSecurityContext;

    @BeforeEach
    void setUp() {
        this.loginController = new LoginController();
        this.mockAuthentication = mock(Authentication.class);
        this.mockSecurityContext = mock(SecurityContext.class);
        SecurityContextHolder.clearContext();
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void testShowLoginForm() {
        when(mockSecurityContext.getAuthentication()).thenReturn(null);
        SecurityContextHolder.setContext(mockSecurityContext);

        when(mockAuthentication.isAuthenticated()).thenReturn(false);
        when(mockAuthentication.getPrincipal()).thenReturn("anonymousUser");

        assertThat(loginController.showLogin()).isEqualTo("login");
    }

    @Test
    void testRedirectIfAuthenticated() {
        when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
        SecurityContextHolder.setContext(mockSecurityContext);

        when(mockAuthentication.isAuthenticated()).thenReturn(true);
        when(mockAuthentication.getPrincipal()).thenReturn(
                new DiaryUser("username", "password", false)
        );

        assertThat(loginController.showLogin()).isEqualTo("redirect:/diary");
    }
}