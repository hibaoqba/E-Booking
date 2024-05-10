package com.example.demo.controller;

import com.example.demo.dto.ReservationDatesResponse;
import com.example.demo.model.Apartment;
import com.example.demo.model.Car;
import com.example.demo.model.Reservation;
import com.example.demo.model.User;
import com.example.demo.repository.CarRepository;
import com.example.demo.repository.ReservationRepository;
import com.example.demo.service.CarService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/cars")
public class CarController {
    @Autowired
    private CarRepository carRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private CarService carService;

    @GetMapping
    public List<Car> getAllCars() {
        return carService.getAllCars();
    }

    @GetMapping("/seller/{sellerId}")
    public ResponseEntity<List<Car>> getSellerCars(@PathVariable Integer sellerId) {
        List<Car> cars = carService.getSellerCars(sellerId);
        return new ResponseEntity<>(cars, HttpStatus.OK);
    }
    @GetMapping("/{carId}/reservationdates")
    public List<ReservationDatesResponse> getCarReservations(@PathVariable Long carId) {
        List<Reservation> reservations = reservationRepository.findReservationsByCarId(carId);

        return reservations.stream()
                .map(reservation -> new ReservationDatesResponse(reservation.getStartDate(), reservation.getEndDate()))
                .collect(Collectors.toList());
    }
    @GetMapping("/seller/{sellerId}/count")
    public ResponseEntity<Long> countCarsBySellerId(@PathVariable Integer sellerId) {
        Long carCount = carService.countCarsBySellerId(sellerId);
        return ResponseEntity.ok(carCount);
    }


    @GetMapping("/city/{city}")
    public ResponseEntity<List<Car>> getCarsByCity(@PathVariable String city) {
        List<Car> cars = carService.getCarsByCity(city);
        return new ResponseEntity<>(cars, HttpStatus.OK);
    }


    @GetMapping("/countAll")
    public ResponseEntity<Long> countAllCars() {
        Long carCount = carRepository.count();
        return ResponseEntity.ok(carCount);
    }

    @GetMapping("/search/{keyword}")
    public ResponseEntity<List<Car>> searchCarsByAddress(@PathVariable String keyword) {
        List<Car> cars = carService.searchCarsByAddress(keyword);
        return ResponseEntity.ok(cars);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Car> getCarById(@PathVariable Long id) {
        Optional<Car> car = carService.getCarById(id);
        return car.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<?> createCar(@RequestBody Car car) {
        User seller = userService.getuserById(car.getSeller().getId().longValue()).orElse(null);
        if (seller == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("seller not found");
        }
        car.setSeller(seller);

        Car savedCar = carService.saveCar(car);
        return new ResponseEntity<>(savedCar, HttpStatus.CREATED);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Car> updateCar(@PathVariable Long id, @RequestBody Car car) {
        Optional<Car> existingCar = carService.getCarById(id);
        if (existingCar.isPresent()) {
            car.setId(id);
            Car updatedCar = carService.saveCar(car);
            return new ResponseEntity<>(updatedCar, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCar(@PathVariable Long id) {
        Optional<Car> existingCar = carService.getCarById(id);
        if (existingCar.isPresent()) {
            carService.deleteCar(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
