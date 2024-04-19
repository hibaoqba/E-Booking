package com.example.demo.controller;

import com.example.demo.model.Apartment;
import com.example.demo.model.Car;
import com.example.demo.model.User;
import com.example.demo.service.ApartmentService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/apartments")
public class ApartmentController {
    @Autowired
    private UserService userService;
    private final ApartmentService apartmentService;

    @Autowired
    public ApartmentController(ApartmentService apartmentService) {
        this.apartmentService = apartmentService;
    }

    @GetMapping
    public ResponseEntity<List<Apartment>> getAllApartments() {
        List<Apartment> apartments = apartmentService.getAllApartments();
        return ResponseEntity.ok(apartments);
    }
    @GetMapping("/seller/{sellerId}")
    public ResponseEntity<List<Apartment>> getSellerApartments(@PathVariable Integer sellerId) {
        List<Apartment> apartments = apartmentService.getSellerApartments(sellerId);
        return new ResponseEntity<>(apartments, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Apartment> getApartmentById(@PathVariable Long id) {
        return apartmentService.getApartmentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createApartment(@RequestBody Apartment apartment) {
        User seller = userService.getuserById(apartment.getSeller().getId().longValue()).orElse(null);
        if (seller == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("seller not found");
        }
        apartment.setSeller(seller);

        Apartment savedCar = apartmentService.saveApartment(apartment);
        return new ResponseEntity<>(savedCar, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteApartment(@PathVariable Long id) {
        apartmentService.deleteApartment(id);
        return ResponseEntity.noContent().build();
    }
}
