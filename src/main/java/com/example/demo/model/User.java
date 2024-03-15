package com.example.demo.model;

import jakarta.annotation.Generated;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="user")
public class User {
    @Id
    @GeneratedValue
    private String id;
    private String nom;
    private String password;
    private String email;
    private String city;
    @Enumerated(EnumType.STRING)
    private Role role;
    private LocalDate birthDate;


}
