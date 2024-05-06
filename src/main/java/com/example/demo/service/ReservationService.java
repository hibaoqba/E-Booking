package com.example.demo.service;

import com.example.demo.model.Payement;
import com.example.demo.model.Reservation;
import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.repository.PayementRepository;
import com.example.demo.repository.ReservationRepository;
import com.example.demo.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.dto.PayReservationRequest;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class ReservationService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PayementRepository payementRepository;
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
        if(reservation.getGps()) {fraisSupp+=100;}
        if(reservation.getChildSeat()) {fraisSupp+=100;}
        double fraisEtabliss=200;
        double totalPrice = fraisSupp+initPrice+fraisEtabliss;
        reservation.setFraisSupp(fraisSupp);
        reservation.setStatus("non payé");
        reservation.setType("voiture");
        reservation.setTitre(reservation.getCar().getBrand()+"-"+reservation.getCar().getModel()+" "+reservation.getCar().getYear());
        reservation.setTotalPrice(totalPrice);
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

    public void payReservation(Long reservationId){
        changeReservationStatus(reservationId,"payé");
        User adminUser = (User) userRepository.findByRole(Role.valueOf("ADMIN"));
        Reservation reservation = reservationRepository.findById(reservationId).get();
        User sellerUser = reservation.getCar().getSeller();
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

    private boolean isValidStatus(String status) {
        // Define your valid statuses here
        return "payé".equals(status) || "non payé".equals(status) || "annulée".equals(status);
    }

    public List<Reservation> getReservationsByCarSellerId(Integer sellerId) {
        return reservationRepository.findByCarSellerId(sellerId);
    }

    public List<Reservation> getReservationsByUserId(Integer userId) {
        return reservationRepository.findByUserId(userId);
    }

    public List<Reservation> getReservationsByCarId(Long carId) {
        return reservationRepository.findReservationsByCarId(carId);
    }

    // Method to get a reservation by ID
    public Reservation getReservationById(Long id) {
        return reservationRepository.findById(id).orElse(null);
    }
    public Long countAllReservations() {
        return reservationRepository.count();
    }

    public Long countReservationsBySellerId(Integer sellerId) {
        return reservationRepository.countReservationsByCarSellerId(sellerId);
    }


    // Method to delete a reservation by ID
    public void deleteReservation(Long id) {
        reservationRepository.deleteById(id);
    }

}
