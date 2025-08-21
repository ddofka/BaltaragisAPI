package org.codeacademy.baltaragisapi.controller;

import org.codeacademy.baltaragisapi.dto.LoginRequest;
import org.codeacademy.baltaragisapi.dto.LoginResponse;
import org.codeacademy.baltaragisapi.web.ProblemSchema;
import org.codeacademy.baltaragisapi.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@Validated
public class AuthController {
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Autowired
    public AuthController(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        try {
            UserDetails user = userDetailsService.loadUserByUsername(request.getUsername());
            if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                throw new BadCredentialsException("Invalid username or password");
            }
            String token = jwtService.createToken(user.getUsername(), Map.of(), user.getAuthorities().stream().findFirst().get().getAuthority());
            return ResponseEntity.ok(new LoginResponse(token));
        } catch (Exception ex) {
            ProblemSchema problem = new ProblemSchema();
            problem.type = "about:blank";
            problem.title = "Unauthorized";
            problem.status = HttpStatus.UNAUTHORIZED.value();
            problem.detail = "Invalid username or password";
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(problem);
        }
    }
}
