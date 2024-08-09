package ac.rs.metropolitan.anteaprimorac5157.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeController {

    @GetMapping
    public String showHomepage(Model model, @AuthenticationPrincipal UserDetails currentUser) {
        model.addAttribute("isLoggedIn", currentUser != null);
        if (currentUser != null) {
            model.addAttribute("username", currentUser.getUsername());
        }

        return "index";
    }
}

