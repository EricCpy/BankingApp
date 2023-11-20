package de.eric.bankingapp.config.auth;


import de.eric.bankingapp.user.model.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Optional;

@Component
@Slf4j
public class JwtUtils {
    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expirationTime}")
    private long jwtExpirationMs;

    public void printKey() {
        System.out.println(jwtSecret.toCharArray());
    }

    public String createToken(User user) {
        Claims claims = Jwts.claims().subject(user.getEmail())
                .add("firstName",user.getFirstName())
                .add("lastName",user.getLastName()).build();
        Date tokenCreateTime = new Date();
        Date tokenValidity = new Date(tokenCreateTime.getTime() + jwtExpirationMs);
        var key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
        return Jwts.builder()
                .claims().add(claims).and()
                .issuedAt(tokenCreateTime)
                .expiration(tokenValidity)
                .signWith(key, Jwts.SIG.HS256)
                .compact();
    }

    private Claims parseJwtClaims(String token) {
        var key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
        JwtParser jwtParser = Jwts.parser().verifyWith(key).build();
        return jwtParser.parseSignedClaims(token).getPayload();
    }

    public Claims resolveClaims(HttpServletRequest req) {
        try {
            Optional<String> token = resolveToken(req);
            return token.map(this::parseJwtClaims).orElse(null);
        } catch (ExpiredJwtException ex) {
            req.setAttribute("expired", ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            req.setAttribute("invalid", ex.getMessage());
            throw ex;
        }
    }

    public Optional<String> resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return Optional.of(bearerToken.substring("Bearer ".length()));
        }
        return Optional.empty();
    }

    public boolean validateClaims(Claims claims) throws AuthenticationException {
        return claims.getExpiration().after(new Date());
    }
}
