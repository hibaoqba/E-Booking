package com.example.demo.controller;

import com.example.demo.auth.AuthenticationRequest;
import com.example.demo.auth.AuthenticationResponse;
import com.example.demo.auth.AuthenticationService;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.service.LogoutService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final LogoutService logoutService;
    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            AuthenticationResponse response = service.register(request);
            return ResponseEntity.ok(response);
        } catch (DataIntegrityViolationException ex) {
            // Catch the exception when a duplicate email is detected
            return ResponseEntity.badRequest().body("Email already exists.");
        }
    }
    // @PostMapping("/register")
    // public ResponseEntity<AuthenticationResponse> register(
    //         @RequestBody RegisterRequest request) {
    //     try {
    //         AuthenticationResponse response = service.register(request);
    //         return ResponseEntity.ok(response);
    //     } catch (UserAlreadyExistsException ex) {
    //         return ResponseEntity.status(HttpStatus.CONFLICT).body("L'utilisateur existe déjà.");
    //     } catch (UserNotFound ex) {
    //         return ResponseEntity.status(HttpStatus.NOT_FOUND).body("L'utilisateur n'a pas été trouvé.");
    //     } catch (Exception ex) {
    //         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Une erreur interne s'est produite.");

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request) {

        return ResponseEntity.ok(service.authenticate(request));
    }

    @PostMapping("/refresh-token")
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        service.refreshToken(request, response);
    }
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
        logoutService.logout(request, response, SecurityContextHolder.getContext().getAuthentication());
        return ResponseEntity.ok("Logged out successfully");
    }

}
