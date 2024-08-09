package ac.rs.metropolitan.anteaprimorac5157.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TokenService {

    private final JwtEncoder encoder;
    private final JwtDecoder decoder;

    @Autowired
    public TokenService(JwtEncoder encoder, JwtDecoder decoder) {
        this.encoder = encoder;
        this.decoder = decoder;
    }

    public String generateToken(Authentication authentication) {
        Instant now = Instant.now();
        String scope = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plus(1, ChronoUnit.DAYS))
                .subject(authentication.getName())
                .claim("scope", scope)
                .claim("id", ((UserDetailsWithID) authentication.getPrincipal()).getId())
                .build();
        return this.encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    public Authentication verifyToken(String token) {
        try {
            Jwt decodedToken = this.decoder.decode(token);
            Map<String, Object> claims = decodedToken.getClaims();
            String username = decodedToken.getSubject();
            String scope = (String) claims.get("scope");
            Collection<GrantedAuthority> authorities = scope == null ? List.of() : Arrays.stream(scope.split(" "))
                    .filter(s -> !s.isBlank())
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());

            JWTUserDetails userDetails = new JWTUserDetails(username, ((Long) claims.get("id")).intValue(), authorities);
            return new JWTAuthentication(userDetails, decodedToken);
        } catch (BadJwtException e) {
            return null;
        }
    }
}