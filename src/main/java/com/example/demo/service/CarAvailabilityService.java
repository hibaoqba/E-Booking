package com.example.demo.service;


import com.example.demo.model.Car;
import com.example.demo.model.Reservation;
import com.example.demo.repository.CarRepository;
import com.example.demo.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class CarAvailabilityService {
    @Autowired
    private CarRepository carRepository;

    @Autowired
    private ReservationRepository reservationRepository;


    // Method to check if a car is available for reservation
    public boolean isCarAvailable(Long carId, LocalDate startDate, LocalDate endDate) {
        Car car = carRepository.findById(carId).orElse(null);
        if (car == null) {
            return false; // Car not found
        }
        // Check if the car is already reserved for the specified period
        List<Car> reservedCars = carRepository.findReservedCars(carId, startDate, endDate);
        return reservedCars.isEmpty();
    }


}
