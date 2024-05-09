package com.example.demo.repository;

import com.example.demo.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface CarRepository extends JpaRepository<Car,Long> {
    Long countCarsBySellerId(Integer sellerId);
    List<Car> findByCity(String city);
    List<Car> findCarsBySellerId(Integer sellerId);
    @Query("SELECT r.car FROM Reservation r WHERE r.car.id = :carId " +
            "AND ((r.startDate BETWEEN :startDate AND :endDate) OR " +
            "(r.endDate BETWEEN :startDate AND :endDate))")
    List<Car> findReservedCars(@Param("carId") Long carId,
                               @Param("startDate") LocalDate startDate,
                               @Param("endDate") LocalDate endDate);
}
