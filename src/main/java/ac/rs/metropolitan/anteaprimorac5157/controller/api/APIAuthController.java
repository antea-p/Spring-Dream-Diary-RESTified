package ac.rs.metropolitan.anteaprimorac5157.controller.api;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ac.rs.metropolitan.anteaprimorac5157.security.TokenService;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class APIAuthController {
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    @Autowired
    public APIAuthController(AuthenticationManager authenticationManager, TokenService tokenService) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

            SecurityContext securityContext = SecurityContextHolder.getContext();
            securityContext.setAuthentication(authentication);

            String token = tokenService.generateToken(authentication);

            return ResponseEntity.ok()
                    .header("Authorization", "Bearer " + token)
                    .body(Map.of("message", "Login successful", "token", token));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", "Login failed"));
        }
    }

    @GetMapping("/check")
    public ResponseEntity<?> isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean authenticated = false;
        if (authentication != null) {
            authenticated = authentication.isAuthenticated();
        }
        return ResponseEntity.ok().body(Map.of("authenticated", authenticated));
    }

    public static class LoginRequest {
        private String username;
        private String password;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}
