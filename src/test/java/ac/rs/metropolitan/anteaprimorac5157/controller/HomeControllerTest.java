package ac.rs.metropolitan.anteaprimorac5157.controller;

import ac.rs.metropolitan.anteaprimorac5157.entity.DiaryUser;
import ac.rs.metropolitan.anteaprimorac5157.security.DiaryUserDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.Model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class HomeControllerTest {

    private HomeController homeController;
    private DiaryUserDetails userDetails;
    private Model model;

    @BeforeEach
    void setUp() {
        this.homeController = new HomeController();
        this.model = mock(Model.class);
    }

    @Test
    void testShowHomeForLoggedInUser() {
        this.userDetails = new DiaryUserDetails(new DiaryUser(
                1, "omori", "password", false)
        );

        assertThat(homeController.showHomepage(model, userDetails)).isEqualTo("index");
        verify(model).addAttribute("isLoggedIn", true);
        verify(model).addAttribute("username", "omori");
    }

    @Test
    void testShowHomeForNonLoggedInUser() {
        assertThat(homeController.showHomepage(model, userDetails)).isEqualTo("index");
        verify(model).addAttribute("isLoggedIn", false);
        verify(model, times(0)) // provjerava da addAttribute NIJE pozvan
                .addAttribute(eq("username"), anyString());
    }
}