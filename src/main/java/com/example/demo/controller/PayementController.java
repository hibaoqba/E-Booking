package com.example.demo.controller;

import com.example.demo.model.Payement;
import com.example.demo.service.PayementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/payments")
public class PayementController {

    private final PayementService payementService;

    @Autowired
    public PayementController(PayementService payementService) {
        this.payementService = payementService;
    }

    @GetMapping
    public ResponseEntity<List<Payement>> getAllPayements() {
        List<Payement> payements = payementService.getAllPayements();
        return new ResponseEntity<>(payements, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Payement> getPayementById(@PathVariable Long id) {
        Optional<Payement> payement = payementService.getPayementById(id);
        return payement.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Payement>> getPayementsByUserId(@PathVariable Integer userId) {
        List<Payement> payements = payementService.getPayementsByUserId(userId);
        return new ResponseEntity<>(payements, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Payement> createPayement(@RequestBody Payement payement) {
        Payement createdPayement = payementService.createPayement(payement);
        return new ResponseEntity<>(createdPayement, HttpStatus.CREATED);
    }

    //total earnings from paid reservs of a user, either a seller or an admin
    @GetMapping("/user/{userId}/totalEarningAmount")
    public ResponseEntity<Double> getTotalEarningAmountByUserId(@PathVariable Integer userId) {
        Double totalEarningAmount = payementService.getTotalEarningAmountByUserId(userId);
        return new ResponseEntity<>(totalEarningAmount, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Payement> updatePayement(@PathVariable Long id, @RequestBody Payement payement) {
        Payement updatedPayement = payementService.updatePayement(id, payement);
        if (updatedPayement != null) {
            return new ResponseEntity<>(updatedPayement, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePayement(@PathVariable Long id) {
        payementService.deletePayement(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
