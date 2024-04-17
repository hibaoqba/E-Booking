package com.example.demo.controller;

import com.example.demo.auth.AuthenticationService;
import com.example.demo.dto.ChangePasswordRequest;
import com.example.demo.model.User;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private AuthenticationService authenticationService;

    @GetMapping("/currentUser")
    public ResponseEntity<User> getCurrentUser() {
        User user = authenticationService.getCurrentUser();
        if (user == null) {
            // Handle the case when no user is authenticated
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(user);
    }

    @PostMapping("/change_password")
    public ResponseEntity<String> changePassword(@RequestBody ChangePasswordRequest request) {
        authenticationService.changePassword(authenticationService.getCurrentUser().getEmail(), request);
        return ResponseEntity.ok("Password changed successfully.");
    }

    @PutMapping("/{email}")
    public ResponseEntity<User> updateUser(@PathVariable String email, @RequestBody User updateUser) {
        Optional<User> existingUserOptional = userService.getuserByEmail(email);
        if (existingUserOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User existingUser = existingUserOptional.get();
        // Update only the fields that are provided in the updateUser object
        if (updateUser.getFirstname() != null) {
            existingUser.setFirstname(updateUser.getFirstname());
        }
        if (updateUser.getLastname() != null) {
            existingUser.setLastname(updateUser.getLastname());
        }
        // Update other fields only if they are provided in the updateUser object
        if (updateUser.getPassword() != null) {
            existingUser.setPassword(updateUser.getPassword());
        }
        if (updateUser.getEmail() != null) {
            existingUser.setEmail(updateUser.getEmail());
        }
        if (updateUser.getPhoneNumber() != null) {
            existingUser.setPhoneNumber(updateUser.getPhoneNumber());
        }
        if (updateUser.getRole() != null) {
            existingUser.setRole(updateUser.getRole());
        }
        if (updateUser.getDetails() != null) {
            existingUser.setDetails(updateUser.getDetails());
        }
        if (updateUser.getAddress() != null) {
            existingUser.setAddress(updateUser.getAddress());
        }
        if (updateUser.getBirthDate() != null) {
            existingUser.setBirthDate(updateUser.getBirthDate());
        }
        // Set other fields as needed

        try {
            User updatedUser = userService.updateUser(existingUser);
            return ResponseEntity.ok(updatedUser);
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build(); // Handle conflict due to duplicate email
        }
    }


}
