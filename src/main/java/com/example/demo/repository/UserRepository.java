package com.example.demo.repository;

import com.example.demo.model.Role;
import com.example.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository  extends JpaRepository<User,String> {
    Optional<User> deleteUserById(Integer id);
    Optional<User> findByEmail(String email);
    List<User> findByRole(Role role);
    @Query("SELECT COUNT(u) FROM User u WHERE u.role = 'CLIENT'")
    long countClients();

    @Query("SELECT COUNT(u) FROM User u WHERE u.role = 'CARSELLER'")
    long countCarSellers();

    @Query("SELECT COUNT(u) FROM User u WHERE u.role = 'APARTMENTSELLER'")
    long countApartmentSellers();
}
