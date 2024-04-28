package com.example.demo.repository;

import com.example.demo.model.Apartment;
import com.example.demo.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ApartmentRepository extends JpaRepository<Apartment,Long> {
    Long countApartmentBySellerId(Integer sellerId);

    List<Apartment> findApartmentsBySellerId(Integer sellerId);
    @Query("SELECT ar.apartment FROM AptReservation ar " +
            "WHERE ar.apartment.id = :apartmentId " +
            "AND ((ar.startDate BETWEEN :startDate AND :endDate) OR " +
            "(ar.endDate BETWEEN :startDate AND :endDate))")
    List<Apartment> findReservedApartments(@Param("apartmentId") Long apartmentId,
                                           @Param("startDate") LocalDate startDate,
                                           @Param("endDate") LocalDate endDate);
}
