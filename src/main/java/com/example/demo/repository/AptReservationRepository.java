package com.example.demo.repository;

import com.example.demo.model.AptReservation;
import com.example.demo.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AptReservationRepository extends JpaRepository<AptReservation,Long> {
    Long countAptReservationsByApartmentSellerId(Integer sellerId);
    List<AptReservation> findByApartmentId(Long apartmentId);
    List<AptReservation> findByUserId(Integer userId);
    List<AptReservation> findByApartmentSellerId(Integer sellerId);
    void deleteReservationsByApartmentId(Long apartmentId);

}
