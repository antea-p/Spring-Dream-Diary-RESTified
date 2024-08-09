package ac.rs.metropolitan.anteaprimorac5157.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;

public class JWTFilter extends HttpFilter {
    private final TokenService tokenService;

    public JWTFilter(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String token = request.getHeader("Authorization");

        if (token == null) {
            chain.doFilter(request, response);
            return;
        }

        // Remove "Bearer "
        Authentication auth = tokenService.verifyToken(token.substring(7));
        SecurityContextHolder.getContext().setAuthentication(auth);
        chain.doFilter(request, response);
    }
}
