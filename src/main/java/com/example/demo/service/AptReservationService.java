package com.example.demo.service;

import com.example.demo.model.AptReservation;
import com.example.demo.model.Reservation;
import com.example.demo.repository.AptReservationRepository;
import com.example.demo.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class AptReservationService {
    @Autowired
    private AptReservationRepository aptReservationRepository;
    @Autowired
    private ApartmentAvailabilityService apartmentAvailabilityService;

    public AptReservation saveAptReservation(AptReservation aptReservation) {
        //        Check if the car is available before making the reservation
        boolean isAvailable = apartmentAvailabilityService.isApartmentAvailable(aptReservation.getApartment().getId(), aptReservation.getStartDate(), aptReservation.getEndDate());
        if (!isAvailable) {
            throw new RuntimeException("Apartment is not available for the specified period");
        }
        long days = aptReservation.getStartDate().until(aptReservation.getEndDate()).getDays()+1;

        aptReservation.setDays(Math.toIntExact(days));
        double aptPrice=aptReservation.getApartment().getPrice();
        double initPrice=aptPrice*days;
        aptReservation.setInitPrice(initPrice);
        aptReservation.setCmndDate(LocalDate.now());
        double fraisSupp=0;
        if(aptReservation.getGps()) {fraisSupp+=200;}
        if(aptReservation.getChildSeat()) {fraisSupp+=100;}
        aptReservation.setFraisSupp(fraisSupp);
        aptReservation.setStatus("non payé");
        aptReservation.setType("apartment");
        aptReservation.setTitre(aptReservation.getApartment().getTitre()+"-"+aptReservation.getApartment().getCity());
        aptReservation.setTotalPrice(fraisSupp+initPrice);
        return aptReservationRepository.save(aptReservation);
    }


    public List<AptReservation> getAllAptReservations() {
        return aptReservationRepository.findAll();
    }


    public List<AptReservation> getReservationsByApartmentSellerId(Integer sellerId) {
        return aptReservationRepository.findByApartmentSellerId(sellerId);
    }

    public List<AptReservation> getReservationsByUserId(Integer userId) {
        return aptReservationRepository.findByUserId(userId);
    }


    // Method to get a AptReservation by ID
    public AptReservation getAptReservationById(Long id) {
        return aptReservationRepository.findById(id).orElse(null);
    }

    // Method to delete a AptReservation by ID
    public void deleteAptReservation(Long id) {
        aptReservationRepository.deleteById(id);
    }


}
