package com.example.demo.controller;

import com.example.demo.model.Apartment;
import com.example.demo.model.ApartmentWish;
import com.example.demo.model.User;
import com.example.demo.service.ApartmentService;
import com.example.demo.service.ApartmentWishService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/apartmentWish")
public class ApartmentWishController {
    @Autowired
    private ApartmentWishService apartmentWishService;
    @Autowired
    private ApartmentService apartmentService;
    @Autowired
    private UserService userService;

    @GetMapping("/user")
    public ResponseEntity<List<ApartmentWish>> getUserApartmentWishes(@RequestParam Long userId) {
        User user = userService.getuserById(userId).orElse(null);
        List<ApartmentWish> apartmentWishes = apartmentWishService.getAllWishesByUser(user);
        return ResponseEntity.ok(apartmentWishes);
    }

    @PostMapping("/add")
    public ResponseEntity<String> addApartmentWish(@RequestParam Long userId, @RequestParam Long apartmentId) {
        User user = userService.getuserById(userId).orElse(null); // Assuming a method to get user by ID
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        Apartment apartment = apartmentService.getApartmentById(apartmentId).orElse(null);
        if (apartment == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Apartment not found");
        }
        apartmentWishService.addWish(user, apartment);
        return ResponseEntity.status(HttpStatus.CREATED).body("Apartment wish added successfully");
    }


    @DeleteMapping("/remove")
    public ResponseEntity<String> removeApartmentWish(@RequestParam Long userId, @RequestParam Long apartmentId) {
        User user = userService.getuserById(userId).orElse(null);
        Apartment apartment = apartmentService.getApartmentById(apartmentId).orElse(null);
        if (apartment == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Apartment not found");
        }
        apartmentWishService.removeWish(user, apartment);
        return ResponseEntity.ok("Apartment wish removed successfully");
    }
    @GetMapping("/isapartmentwishlist")
    public ResponseEntity<Boolean> isApartmentInWishlist(@RequestParam Long userId, @RequestParam Long apartmentId) {
        User user = userService.getuserById(userId).orElse(null);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(false);
        }
        Apartment apartment = apartmentService.getApartmentById(apartmentId).orElse(null);
        if (apartment == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(false);
        }
        List<ApartmentWish> userApartmentWishes = apartmentWishService.getAllWishesByUser(user);
        boolean isWish = userApartmentWishes.stream().anyMatch(wish -> wish.getApartment().equals(apartment));
        return ResponseEntity.ok(isWish);
    }

}
