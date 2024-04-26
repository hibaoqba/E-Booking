package com.example.demo.controller;

import com.example.demo.dto.PayReservationRequest;
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
import java.util.NoSuchElementException;

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

    //endPoint to get reservation by userId
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Reservation>> getReservationsByUserId(@PathVariable Integer userId) {
        List<Reservation> reservations = reservationService.getReservationsByUserId(userId);
        return new ResponseEntity<>(reservations, HttpStatus.OK);
    }

    @GetMapping("/count")
    public ResponseEntity<Long> countAllReservations() {
        Long reservCount = reservationService.countAllReservations();
        return ResponseEntity.ok(reservCount);
    }

    @PostMapping("/status/{reservationId}/{status}")
    public ResponseEntity<String> changeReservationStatus(@PathVariable Long reservationId, @PathVariable String status) {
        try {
            reservationService.changeReservationStatus(reservationId, status);
            return new ResponseEntity<>("Reservation status changed successfully", HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>("Reservation not found", HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>("Invalid status", HttpStatus.BAD_REQUEST);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/pay/{reservationId}")
    public ResponseEntity<?> payReservation(@PathVariable Long reservationId) {
        try {
            reservationService.payReservation(reservationId);
            return new ResponseEntity<>("Reservation successfully paid", HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>("Reservation not found", HttpStatus.NOT_FOUND);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/seller/{sellerId}")
    public ResponseEntity<List<Reservation>> getReservationsByCarSellerId(@PathVariable Integer sellerId) {
        List<Reservation> reservations = reservationService.getReservationsByCarSellerId(sellerId);
        return new ResponseEntity<>(reservations, HttpStatus.OK);
    }

    @GetMapping("/car/{carId}")
    public ResponseEntity<List<Reservation>> getReservationsByCarId(@PathVariable Long carId) {
        List <Reservation> reservation = reservationService.getReservationsByCarId(carId);
        if (reservation == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        }
        return new ResponseEntity<>(reservation, HttpStatus.OK);
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
