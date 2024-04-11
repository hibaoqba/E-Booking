package com.example.demo.model;

import jakarta.annotation.Generated;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="user")
public class User   implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String firstname;
    private String lastname;
    private String avatar;
    private String password;
    private String email;
    private String phoneNumber;
    @Enumerated(EnumType.STRING)
    private Role role;
    @Column(columnDefinition = "TEXT")
    private String details;
    @Embedded
    private Address address;
    private LocalDate birthDate;

    @Override
    // new SimpleGrantedAuthority(...): C'est un objet qui implémente l'interface
    // GrantedAuthority et représente une autorité attribuée à un utilisateur.
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    // vérifie si le compte de l'utilisateur n'a pas expiré
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // vérifie si le compte de l'utilisateur n'est pas verrouillé
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // vérifie si les informations d'identification (comme le mot de passe) de
    // l'utilisateur n'ont pas expiré
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // vérifie si le compte de l'utilisateur est activé
    @Override
    public boolean isEnabled() {
        return true;
    }

}
