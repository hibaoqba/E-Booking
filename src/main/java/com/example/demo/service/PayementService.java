package com.example.demo.service;

import com.example.demo.model.Payement;
import com.example.demo.repository.PayementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class PayementService {

    private final PayementRepository payementRepository;

    @Autowired
    public PayementService(PayementRepository payementRepository) {
        this.payementRepository = payementRepository;
    }

    public List<Payement> getAllPayements() {
        return payementRepository.findAll();
    }

    public Optional<Payement> getPayementById(Long id) {
        return payementRepository.findById(id);
    }

    public Payement createPayement(Payement payement) {
        payement.setPayementDate(LocalDate.now());
        return payementRepository.save(payement);
    }

    public Payement updatePayement(Long id, Payement payement) {
        if (payementRepository.existsById(id)) {
            payement.setId(id);
            return payementRepository.save(payement);
        } else {
            return null; // or throw an exception indicating that the entity does not exist
        }
    }

    public void deletePayement(Long id) {
        payementRepository.deleteById(id);
    }
}
