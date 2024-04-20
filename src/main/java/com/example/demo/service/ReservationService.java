package com.example.demo.service;

import com.example.demo.model.Reservation;
import com.example.demo.repository.ReservationRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.dto.PayReservationRequest;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.NoSuchElementException;

@Service
public class ReservationService {
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private CarAvailabilityService carAvailabilityService;
    public Reservation saveReservation(Reservation reservation) {
 //        Check if the car is available before making the reservation
        boolean isAvailable = carAvailabilityService.isCarAvailable(reservation.getCar().getId(), reservation.getStartDate(), reservation.getEndDate());
        if (!isAvailable) {
            throw new RuntimeException("Car is not available for the specified period");
        }
        long days = reservation.getStartDate().until(reservation.getEndDate()).getDays()+1;

        reservation.setDays(Math.toIntExact(days));
        double carPrice=reservation.getCar().getPrice();
        double initPrice=carPrice*days;
        reservation.setInitPrice(initPrice);
        reservation.setCmndDate(LocalDate.now());
        double fraisSupp=0;
        if(reservation.getGps()) {fraisSupp+=200;}
        if(reservation.getChildSeat()) {fraisSupp+=100;}
        reservation.setFraisSupp(fraisSupp);
        reservation.setStatus("non payé");
        reservation.setType("voiture");
        reservation.setTitre(reservation.getCar().getBrand()+"-"+reservation.getCar().getModel()+" "+reservation.getCar().getYear());
        reservation.setTotalPrice(fraisSupp+initPrice);
        reservation.setRemainPrice(reservation.getTotalPrice());
        return reservationRepository.save(reservation);
    }

    // Method to get all reservations
    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    @Transactional
    public void changeReservationStatus(Long reservationId, String status) {
        Reservation reservation = reservationRepository.findById(reservationId)
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

        reservationRepository.save(reservation);
    }

    private boolean isValidStatus(String status) {
        // Define your valid statuses here
        return "payé".equals(status) || "non payé".equals(status) || "cancelled".equals(status);
    }

    public List<Reservation> getReservationsByCarSellerId(Integer sellerId) {
        return reservationRepository.findByCarSellerId(sellerId);
    }

    public List<Reservation> getReservationsByUserId(Integer userId) {
        return reservationRepository.findByUserId(userId);
    }

    public Reservation getReservationsByCarId(Long carId) {
        return reservationRepository.findByCarId(carId);    }

    // Method to get a reservation by ID
    public Reservation getReservationById(Long id) {
        return reservationRepository.findById(id).orElse(null);
    }

    // Method to delete a reservation by ID
    public void deleteReservation(Long id) {
        reservationRepository.deleteById(id);
    }

}
