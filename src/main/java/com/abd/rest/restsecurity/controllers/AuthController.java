package com.abd.rest.restsecurity.controllers;

import com.abd.rest.restsecurity.config.JwtTokenProvider;
import com.abd.rest.restsecurity.model.User;
import com.abd.rest.restsecurity.repositories.UserRepository;
import com.abd.rest.restsecurity.rest.dto.AuthBody;
import com.abd.rest.restsecurity.services.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    UserRepository users;

    @Autowired
    private CustomUserDetailsService userService;

    @SuppressWarnings("rawtypes")
    @PostMapping("/login")
    public ResponseEntity login(@RequestBody AuthBody data) {
        try {
            String username = data.getEmail();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, data.getPassword()));
            String token = jwtTokenProvider.createToken(username, this.users.findByEmail(username).getRoles());
            String refreshToken = jwtTokenProvider.createRefreshToken(username, this.users.findByEmail(username).getRoles());
            Map<Object, Object> model = new LinkedHashMap<>();
            model.put("username", username);
            model.put("token", token);
            model.put("refresh-token", refreshToken);
            return ok(model);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid email/password supplied");
        }
    }

    @GetMapping("/refresh-token")
    public ResponseEntity refreshToken(HttpServletRequest req){
        String refreshToken = jwtTokenProvider.resolveRefreshToken( req);
        String username = jwtTokenProvider.getUsername(refreshToken);
        jwtTokenProvider.removeToken(refreshToken);
        String token = jwtTokenProvider.createToken(username, this.users.findByEmail(username).getRoles());
        refreshToken = jwtTokenProvider.createRefreshToken(username, this.users.findByEmail(username).getRoles());
        Map<Object, Object> model = new LinkedHashMap<>();
        model.put("username", username);
        model.put("token", token);
        model.put("refresh-token", refreshToken);
        return ok(model);
    }

    @SuppressWarnings("rawtypes")
    @PostMapping("/register")
    public ResponseEntity register(@RequestBody User user) {
        User userExists = userService.findUserByEmail(user.getEmail());
        if (userExists != null) {
            throw new BadCredentialsException("User with username: " + user.getEmail() + " already exists");
        }
        userService.saveUser(user);
        Map<Object, Object> model = new HashMap<>();
        model.put("message", "User registered successfully");
        return ok(model);
    }
}
