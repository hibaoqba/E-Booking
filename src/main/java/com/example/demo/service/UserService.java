package com.example.demo.service;

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

    public Optional<User> getuserById(Long id) {
        return userRepository.findById(String.valueOf(id));
    }

    public User saveuser(User user) {
        return userRepository.save(user);
    }

    public void deleteuser(Long id) {
        userRepository.deleteById(String.valueOf(id));
    }}
