package com.example.demo.controller;

import com.example.demo.model.Apartment;
import com.example.demo.model.AptReservation;
import com.example.demo.model.Reservation;
import com.example.demo.model.User;
import com.example.demo.service.ApartmentService;
import com.example.demo.service.AptReservationService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;


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

    //count upaid
    @GetMapping("/{sellerId}/reservations/nonpaid/count")
    public Long countNonPaidReservationsForSeller(@PathVariable Integer sellerId) {
        return aptReservationService.countNonPaidReservationsBySellerId(sellerId);
    }

    // count paid
    @GetMapping("/{sellerId}/reservations/paid/count")
    public Long countPaidReservationsForSeller(@PathVariable Integer sellerId) {
        return aptReservationService.countPaidReservationsBySellerId(sellerId);
    }


    @PostMapping("/status/{aptReservationId}/{status}")
    public ResponseEntity<String> changeReservationStatus(@PathVariable Long aptReservationId, @PathVariable String status) {
        try {
            aptReservationService.changeReservationStatus(aptReservationId, status);
            return new ResponseEntity<>("Reservation status changed successfully", HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>("Reservation not found", HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>("Invalid status", HttpStatus.BAD_REQUEST);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/pay/{aptReservationId}")
    public ResponseEntity<?> payReservation(@PathVariable Long aptReservationId) {
        try {
            aptReservationService.payReservation(aptReservationId);
            return new ResponseEntity<>("Reservation successfully paid", HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>("Reservation not found", HttpStatus.NOT_FOUND);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/count")
    public ResponseEntity<Long> countAllReservations() {
        Long reservCount = aptReservationService.countAllReservations();
        return ResponseEntity.ok(reservCount);
    }
    @GetMapping("/seller/count/{sellerId}")
    public ResponseEntity<Long> countAllReservations(@PathVariable Integer sellerId){
        Long reservCountBySeller = aptReservationService.countReservationsBySellerId(sellerId);
        return ResponseEntity.ok(reservCountBySeller);
    }

    // Endpoint to get apartment reservations by userId
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AptReservation>> getAptReservationsByUserId(@PathVariable Integer userId) {
        List<AptReservation> reservations = aptReservationService.getReservationsByUserId(userId);
        return new ResponseEntity<>(reservations, HttpStatus.OK);
    }

    // Endpoint to get apartment reservations by sellerId
    @GetMapping("/seller/{sellerId}")
    public ResponseEntity<List<AptReservation>> getReservationsByApartmentSellerId(@PathVariable Integer sellerId) {
        List<AptReservation> reservations = aptReservationService.getReservationsByApartmentSellerId(sellerId);
        return new ResponseEntity<>(reservations, HttpStatus.OK);
    }

    @GetMapping("/apartment/{apartmentId}")
    public ResponseEntity<List<AptReservation>> getReservationsByApartmentId(@PathVariable Long apartmentId) {
        List<AptReservation> aptReservation = aptReservationService.getAptReservationsByApartmentId(apartmentId);
        if (aptReservation == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(aptReservation, HttpStatus.OK);
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
            aptReservation.setUser(user);
            aptReservation.setApartment(apartment);
            AptReservation savedReservation = aptReservationService.saveAptReservation(aptReservation);
            return new ResponseEntity<>(savedReservation, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/paid_reservation")
    public ResponseEntity<?> createPaidAptReservation(@RequestBody AptReservation aptReservation) {
        try {
            User user = userService.getuserById(aptReservation.getUser().getId().longValue()).orElse(null);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }
            Apartment apartment = apartmentService.getApartmentById(aptReservation.getApartment().getId()).orElse(null);
            if (apartment == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Apartment not found");
            }
            aptReservation.setUser(user);
            aptReservation.setApartment(apartment);
            AptReservation savedReservation = aptReservationService.savePaidAptReservation(aptReservation);
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
