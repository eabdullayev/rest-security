package com.abd.rest.restsecurity.config;

import com.abd.rest.restsecurity.model.Role;
import com.abd.rest.restsecurity.services.CustomUserDetailsService;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

@Component
public class JwtTokenProvider {
    @Value("${security.jwt.token.secret-key:secret}")
    private String secretKey = "secret";

    @Value("${security.jwt.token.expire-length:3600000}")
    private long validityInMilliseconds = 3600000; // 1h

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public String createToken(String username, Set<Role> set) {
        Claims claims = Jwts.claims().setSubject(username);
        claims.put("roles", set);
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public String createRefreshToken(String username, Set<Role> set) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + 3*validityInMilliseconds);

        Claims claims = Jwts.claims().setSubject(username);
        claims.put("roles", set);

        String refreshToken = Jwts.builder()
                .setClaims(claims)
                .setId(UUID.randomUUID().toString())
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        return refreshToken;
    }

    Authentication getAuthentication(String token) {
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(getUsername(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getUsername(String token) {
        return getClaims(token).getBody().getSubject();
    }

    public String resolveRefreshToken(HttpServletRequest req) {
        String bearerToken = req.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Refresh-Token ")) {
            return bearerToken.substring(14);
        }
        return null;
    }

    public void removeToken(String token) {
        getClaims(token).getBody().setExpiration(new Date());
    }

    String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    boolean validateToken(String token) {
        return !getClaims(token).getBody().getExpiration().before(new Date());
    }

    private Jws<Claims> getClaims(String token) {
        try {
            return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
        } catch (JwtException | IllegalArgumentException e) {
            throw new JwtException("Expired or invalid JWT token");
        }
    }
}
