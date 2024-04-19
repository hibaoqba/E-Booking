package com.example.demo.service;

import com.example.demo.model.Apartment;
import com.example.demo.repository.ApartmentRepository;
import com.example.demo.repository.AptReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ApartmentAvailabilityService {
    @Autowired
    private ApartmentRepository apartmentRepository;
    @Autowired
    private AptReservationRepository aptReservationRepository;
    public boolean isApartmentAvailable(Long apartmentId, LocalDate startDate, LocalDate endDate) {
        Apartment apartment = apartmentRepository.findById(apartmentId).orElse(null);
        if (apartment == null) {
            return false; // apartment not found
        }
        // Check if the Apartment is already reserved for the specified period
        List<Apartment> reservedApartments = apartmentRepository.findReservedApartments(apartmentId, startDate, endDate);
        return reservedApartments.isEmpty();
    }
}
