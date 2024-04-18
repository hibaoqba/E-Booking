package com.example.demo.repository;

import com.example.demo.model.AptReservation;
import com.example.demo.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AptReservationRepository extends JpaRepository<AptReservation,Long> {
    List<AptReservation> findByUserId(Integer userId);
    List<AptReservation> findByApartmentSellerId(Integer sellerId);
}
