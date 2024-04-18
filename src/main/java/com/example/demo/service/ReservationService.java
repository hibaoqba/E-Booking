package com.example.demo.service;

import com.example.demo.model.Reservation;
import com.example.demo.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReservationService {
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private CarAvailabilityService carAvailabilityService;
    public Reservation saveReservation(Reservation reservation) {
//         Check if the car is available before making the reservation
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
        reservation.setTotalPrice(fraisSupp+initPrice);
            return reservationRepository.save(reservation);
    }

    // Method to get all reservations
    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    public List<Reservation> getReservationsByUserId(Integer userId) {
        return reservationRepository.findByUserId(userId);
    }


    // Method to get a reservation by ID
    public Reservation getReservationById(Long id) {
        return reservationRepository.findById(id).orElse(null);
    }

    // Method to delete a reservation by ID
    public void deleteReservation(Long id) {
        reservationRepository.deleteById(id);
    }

}
