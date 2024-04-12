package com.example.demo.controller;

import com.example.demo.model.Car;
import com.example.demo.model.CarWish;
import com.example.demo.model.User;
import com.example.demo.service.CarService;
import com.example.demo.service.CarWishService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/carwishes")
public class CarWishController {
    private final CarWishService carWishService;
    private final CarService carService;
    private final UserService userService;
    @Autowired
    public CarWishController(CarWishService carWishService, CarService carService,UserService userService) {
        this.carWishService = carWishService;
        this.carService = carService;
        this.userService=userService;
    }

    @GetMapping("/user")
    public ResponseEntity<List<CarWish>> getUserCarWishes(@RequestParam Long userId) {
        User user = userService.getuserById(userId).orElse(null);
        List<CarWish> carWishes = carWishService.getAllWishesByUser(user);
        return ResponseEntity.ok(carWishes);
    }

    @PostMapping("/add")
    public ResponseEntity<String> addCarWish(@RequestParam Long userId, @RequestParam Long carId) {
        User user = userService.getuserById(userId).orElse(null); // Assuming a method to get user by ID
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        Car car = carService.getCarById(carId).orElse(null);
        if (car == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Car not found");
        }
        carWishService.addWish(user, car);
        return ResponseEntity.status(HttpStatus.CREATED).body("Car wish added successfully");
    }


    @DeleteMapping("/remove")
    public ResponseEntity<String> removeCarWish(@RequestParam Long userId, @RequestParam Long carId) {
        User user = userService.getuserById(userId).orElse(null);
        Car car = carService.getCarById(carId).orElse(null);
        if (car == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Car not found");
        }
        carWishService.removeWish(user, car);
        return ResponseEntity.ok("Car wish removed successfully");
    }
    @GetMapping("/iscarwishlist")
    public ResponseEntity<Boolean> isCarInWishlist(@RequestParam Long userId, @RequestParam Long carId) {
        User user = userService.getuserById(userId).orElse(null);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(false);
        }
        Car car = carService.getCarById(carId).orElse(null);
        if (car == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(false);
        }
        List<CarWish> userCarWishes = carWishService.getAllWishesByUser(user);
        boolean isWish = userCarWishes.stream().anyMatch(wish -> wish.getCar().equals(car));
        return ResponseEntity.ok(isWish);
    }

}
