package com.example.demo.repository;

import com.example.demo.model.Role;
import com.example.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository  extends JpaRepository<User,String> {
    Optional<User> deleteUserById(Integer id);
    Optional<User> findByEmail(String email);
    List<User> findByRole(Role role);

}
