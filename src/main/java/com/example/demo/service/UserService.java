package com.example.demo.service;

import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private  UserRepository userRepository;

    public List<User> getAllusers() {
        return userRepository.findAll();
    }

    public Optional<User> getuserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> getuserById(Long id) {
        return userRepository.findById(String.valueOf(id));
    }

    public User saveuser(User user) {
        return userRepository.save(user);
    }

    public User updateUser(User user) {
        return userRepository.save(user);
    }
    public Long countAllUsers() {
        return userRepository.count();
    }

    public List<User> getAllCarSellers() {
        return userRepository.findByRole(Role.CARSELLER);
    }

    public List<User> getAllApartmentSellers() {
        return userRepository.findByRole(Role.APARTMENTSELLER);
    }


    public void deleteUser(Integer id) {

        userRepository.deleteUserById(id);
    }}
