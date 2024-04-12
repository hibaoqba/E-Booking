package com.example.demo.controller;

import com.example.demo.model.Reservation;
import com.example.demo.model.User;
import com.example.demo.model.Car;

import com.example.demo.service.CarService;
import com.example.demo.service.ReservationService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    @Autowired
    private UserService userService;
    @Autowired
    private CarService carService;
    private final ReservationService reservationService;

    @Autowired
    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    // Endpoint to get all reservations
    @GetMapping
    public ResponseEntity<List<Reservation>> getAllReservations() {
        List<Reservation> reservations = reservationService.getAllReservations();
        return new ResponseEntity<>(reservations, HttpStatus.OK);
    }

    // Endpoint to get a reservation by ID
    @GetMapping("/{id}")
    public ResponseEntity<Reservation> getReservationById(@PathVariable Long id) {
        Reservation reservation = reservationService.getReservationById(id);
        if (reservation == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(reservation, HttpStatus.OK);
    }

    // Endpoint to create a reservation
    @PostMapping
    public ResponseEntity<?> createReservation(@RequestBody Reservation reservation) {
        try {
            User user = userService.getuserById(reservation.getUser().getId().longValue()).orElse(null);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }

            // Retrieve car by ID
            Car car = carService.getCarById(reservation.getCar().getId()).orElse(null);
            if (car == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Car not found");
            }

            // Set the user and car in the reservation
            reservation.setUser(user);
            reservation.setCar(car);
            Reservation savedReservation = reservationService.saveReservation(reservation);
            return new ResponseEntity<>(savedReservation, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // Endpoint to delete a reservation by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
        reservationService.deleteReservation(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
