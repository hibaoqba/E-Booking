package com.example.demo.controller;

import com.example.demo.auth.AuthenticationRequest;
import com.example.demo.auth.AuthenticationResponse;
import com.example.demo.auth.AuthenticationService;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.service.LogoutService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import com.example.demo.service.ForgotPasswordService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final ForgotPasswordService forgotPasswordService;
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

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request) {

        return ResponseEntity.ok(service.authenticate(request));
    }



    @PostMapping("/forgot-password")
    public String forgotPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        try {
            forgotPasswordService.sendResetLink(email);
            return "Password reset link sent.";
        } catch (MailException | MessagingException e) {
            return "Error sending email.";
        }
    }

    @GetMapping("/reset-password")
    public ResponseEntity<String> showResetPasswordPage(@RequestParam("token") String token) {
        // Returning a simple HTML form as a string
        String htmlForm = "<html>" +
                "<head><title>Reset Password</title></head>" +
                "<body>" +
                "<h1>Reset Password</h1>" +
                "<form action='/api/auth/reset-password' method='post'>" +
                "<input type='hidden' name='token' value='" + token + "' />" +
                "<div>" +
                "<label for='password'>New Password:</label>" +
                "<input type='password' id='password' name='password' required />" +
                "</div>" +
                "<div>" +
                "<button type='submit'>Reset Password</button>" +
                "</div>" +
                "</form>" +
                "</body>" +
                "</html>";
        return ResponseEntity.ok(htmlForm);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> handleResetPassword(@RequestParam("token") String token,
                                                      @RequestParam("password") String password) {
        try {
            forgotPasswordService.resetPassword(token, password);
            return ResponseEntity.ok("Password reset successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid or expired token.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error resetting password.");
        }
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
