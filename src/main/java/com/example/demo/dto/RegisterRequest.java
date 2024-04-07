package com.example.demo.dto;

import com.example.demo.model.Address;
import com.example.demo.model.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    private String firstname;
    private String lastname;
    private String avatar;
    private String password;
    private String email;
    private String phoneNumber;
    private Role role; // Accepts string instead of Role enum
    private String details;
    private Address address;
    private LocalDate birthDate;
}
