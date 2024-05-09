package com.example.demo.service;

import com.example.demo.model.Apartment;
import com.example.demo.model.Car;
import com.example.demo.repository.CarRepository;
import com.example.demo.repository.CarWishRepository;
import com.example.demo.repository.ReservationRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CarService {

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private CarWishRepository carWishRepository;

    public List<Car> getAllCars() {
        return carRepository.findAll();
    }
    public List<Car> getSellerCars(Integer sellerId) {
        return carRepository.findCarsBySellerId(sellerId);
    }


    public List<Car> getCarsByCity(String city) {
        return carRepository.findByCity(city);
    }


    public Long countCarsBySellerId(Integer sellerId) {
        return carRepository.countCarsBySellerId(sellerId);
    }

    public Optional<Car> getCarById(Long id) {
        return carRepository.findById(id);
    }

    public Car saveCar(Car car) {
        return carRepository.save(car);
    }
    @Transactional
    public void deleteCar(Long id) {
        reservationRepository.deleteReservationsByCarId(id);
        carWishRepository.deleteCarWishByCarId(id);
        carRepository.deleteById(id);
    }
}
