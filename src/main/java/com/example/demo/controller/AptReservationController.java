package com.example.demo.controller;

import com.example.demo.model.Apartment;
import com.example.demo.model.AptReservation;
import com.example.demo.model.User;
import com.example.demo.service.ApartmentService;
import com.example.demo.service.AptReservationService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/apt_reservations")
public class AptReservationController {
    @Autowired
    private UserService userService;
    @Autowired
    private ApartmentService apartmentService;
    private final AptReservationService aptReservationService;

    @Autowired
    public AptReservationController(AptReservationService aptReservationService) {
        this.aptReservationService = aptReservationService;
    }

    // Endpoint to get all apartment reservations
    @GetMapping
    public ResponseEntity<List<AptReservation>> getAllAptReservations() {
        List<AptReservation> reservations = aptReservationService.getAllAptReservations();
        return new ResponseEntity<>(reservations, HttpStatus.OK);
    }

    // Endpoint to get apartment reservations by userId
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AptReservation>> getReservationsByUserId(@PathVariable Integer userId) {
        List<AptReservation> reservations = aptReservationService.getReservationsByUserId(userId);
        return new ResponseEntity<>(reservations, HttpStatus.OK);
    }

    // Endpoint to get apartment reservations by sellerId
    @GetMapping("/seller/{sellerId}")
    public ResponseEntity<List<AptReservation>> getReservationsByApartmentSellerId(@PathVariable Integer sellerId) {
        List<AptReservation> reservations = aptReservationService.getReservationsByApartmentSellerId(sellerId);
        return new ResponseEntity<>(reservations, HttpStatus.OK);
    }

    // Endpoint to get an apartment reservation by ID
    @GetMapping("/{id}")
    public ResponseEntity<AptReservation> getAptReservationById(@PathVariable Long id) {
        AptReservation reservation = aptReservationService.getAptReservationById(id);
        if (reservation == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(reservation, HttpStatus.OK);
    }

    // Endpoint to create an apartment reservation
    @PostMapping
    public ResponseEntity<?> createAptReservation(@RequestBody AptReservation aptReservation) {
        try {
            User user = userService.getuserById(aptReservation.getUser().getId().longValue()).orElse(null);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }

            // Retrieve apartment by ID
            Apartment apartment = apartmentService.getApartmentById(aptReservation.getApartment().getId()).orElse(null);
            if (apartment == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Apartment not found");
            }

            // Set the user and apartment in the reservation
            aptReservation.setUser(user);
            aptReservation.setApartment(apartment);
            AptReservation savedReservation = aptReservationService.saveAptReservation(aptReservation);
            return new ResponseEntity<>(savedReservation, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // Endpoint to delete an apartment reservation by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAptReservation(@PathVariable Long id) {
        aptReservationService.deleteAptReservation(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}