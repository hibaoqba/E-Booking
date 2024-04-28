package com.example.demo.service;

import com.example.demo.model.*;
import com.example.demo.model.Payement;
import com.example.demo.repository.AptReservationRepository;
import com.example.demo.repository.PayementRepository;
import com.example.demo.repository.ReservationRepository;
import com.example.demo.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class AptReservationService {
    @Autowired
    private PayementRepository payementRepository;
    @Autowired
    private UserRepository userRepository;
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
        if(aptReservation.getClearning()) {fraisSupp+=60;}
        if(aptReservation.getBreakfasts()) {fraisSupp+=80;}

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

    @Transactional
    public void changeReservationStatus(Long reservationId, String status) {
        AptReservation reservation = aptReservationRepository.findById(reservationId)
                .orElseThrow(NoSuchElementException::new);

        if (!isValidStatus(status)) {
            throw new IllegalArgumentException("Invalid status");
        }

        if (!"non payé".equals(reservation.getStatus()) && "payé".equals(status)) {
            throw new RuntimeException("Reservation is already paid");
        }

        reservation.setStatus(status);

        if ("payé".equals(status)) {
            reservation.setRemainPrice(0.0);
        }

        aptReservationRepository.save(reservation);
    }

    public void payReservation(Long reservationId){
        changeReservationStatus(reservationId,"payé");
        User adminUser = userRepository.findByRole(Role.valueOf("ADMIN"));
        AptReservation reservation = aptReservationRepository.findById(reservationId).get();
        User sellerUser = reservation.getApartment().getSeller();
        if (adminUser == null) {
            throw new IllegalStateException("Admin user not found");
        }
        if (sellerUser == null) {
            throw new IllegalStateException("seller user not found");
        }

        double totalPrice = reservation.getTotalPrice();
        Payement adminPayment = Payement.builder()
                .payementDate(LocalDate.now())
                .earningAmount(200.0)
                .titre("Payment for admin")
                .user(adminUser)
                .build();
        payementRepository.save(adminPayment);
        Payement sellerPayment = Payement.builder()
                .payementDate(LocalDate.now())
                .earningAmount(totalPrice-200)
                .titre("Payment for seller")
                .user(sellerUser)
                .build();
        payementRepository.save(sellerPayment);
    }

    public Long countAllReservations() {
        return aptReservationRepository.count();
    }

    public Long countReservationsBySellerId(Integer sellerId){
        return aptReservationRepository.countAptReservationsByApartmentSellerId(sellerId);
    }

    private boolean isValidStatus(String status) {
        // Define your valid statuses here
        return "payé".equals(status) || "non payé".equals(status) || "cancelled".equals(status);
    }

    // Method to get a AptReservation by ID
    public AptReservation getAptReservationById(Long id) {
        return aptReservationRepository.findById(id).orElse(null);
    }

    public List<AptReservation> getAptReservationsByApartmentId(Long apartmentId) {
        return aptReservationRepository.findByApartmentId(apartmentId);
    }


    // Method to delete a AptReservation by ID
    public void deleteAptReservation(Long id) {
        aptReservationRepository.deleteById(id);
    }


}
