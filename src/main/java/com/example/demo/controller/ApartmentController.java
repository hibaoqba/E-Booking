package com.example.demo.controller;

import com.example.demo.dto.ReservationDatesResponse;
import com.example.demo.model.*;
import com.example.demo.model.Apartment;
import com.example.demo.repository.ApartmentRepository;
import com.example.demo.repository.AptReservationRepository;
import com.example.demo.service.ApartmentService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/apartments")
public class ApartmentController {
    @Autowired
    private ApartmentRepository apartmentRepository;

    @Autowired
    private AptReservationRepository aptReservationRepository;
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

    @GetMapping("/city/{city}")
    public ResponseEntity<List<Apartment>> getApartmentByCity(@PathVariable String city) {
        List<Apartment> apartments = apartmentService.getApartmentsByCity(city);
        return new ResponseEntity<>(apartments, HttpStatus.OK);
    }

    @GetMapping("/countAll")
    public ResponseEntity<Long> countAllApartments() {
        Long apartmentCount = apartmentRepository.count();
        return ResponseEntity.ok(apartmentCount);
    }
    @PutMapping("/{id}")
    public ResponseEntity<Apartment> updateApartment(@PathVariable Long id, @RequestBody Apartment apartment) {
        Optional<Apartment> existingApartment = apartmentService.getApartmentById(id);
        if (existingApartment.isPresent()) {
            apartment.setId(id);
            Apartment updatedApartment =apartmentService.saveApartment(apartment);
            return new ResponseEntity<>(updatedApartment, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<Apartment> getApartmentById(@PathVariable Long id) {
        return apartmentService.getApartmentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{apartmentId}/reservationdates")
    public List<ReservationDatesResponse> getApartmentReservations(@PathVariable Long apartmentId) {
        List<AptReservation> reservations = aptReservationRepository.findByApartmentId(apartmentId);

        return reservations.stream()
                .map(reservation -> new ReservationDatesResponse(reservation.getStartDate(), reservation.getEndDate()))
                .collect(Collectors.toList());
    }
    @GetMapping("/seller/{sellerId}/count")
    public ResponseEntity<Long> countApartmentsBySellerId(@PathVariable Integer sellerId) {
        Long apartmentCount = apartmentService.countApartmentsBySellerId(sellerId);
        return ResponseEntity.ok(apartmentCount);
    }
    
    @PostMapping
    public ResponseEntity<?> createApartment(@RequestBody Apartment apartment) {
        User seller = userService.getuserById(apartment.getSeller().getId().longValue()).orElse(null);
        if (seller == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("seller not found");
        }
        apartment.setSeller(seller);

        Apartment savedApartment = apartmentService.saveApartment(apartment);
        return new ResponseEntity<>(savedApartment, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteApartment(@PathVariable Long id) {
        apartmentService.deleteApartment(id);
        return ResponseEntity.noContent().build();
    }
}
