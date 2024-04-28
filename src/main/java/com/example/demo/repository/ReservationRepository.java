package com.example.demo.repository;


import com.example.demo.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation,Long> {
    Long countReservationsByCarSellerId(Integer sellerId);

    List<Reservation> findByUserId(Integer userId);
    List<Reservation> findByCarSellerId(Integer sellerId);
    void deleteReservationsByCarId(Long carId);
    List<Reservation> findReservationsByCarId(Long carId);
}
