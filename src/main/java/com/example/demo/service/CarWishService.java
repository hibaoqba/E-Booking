package com.example.demo.service;

import com.example.demo.model.Car;
import com.example.demo.model.CarWish;
import com.example.demo.model.User;
import com.example.demo.repository.CarWishRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CarWishService {
    private final CarWishRepository carWishRepository;

    @Autowired
    public CarWishService(CarWishRepository carWishRepository) {
        this.carWishRepository = carWishRepository;
    }

    public List<CarWish> getAllWishesByUser(User user) {
        return carWishRepository.findByUser(user);
    }

    public void addWish(User user, Car car) {
        CarWish carWish = new CarWish();
        carWish.setUser(user);
        carWish.setCar(car);
        carWishRepository.save(carWish);
    }

    public void removeWish(User user, Car car) {
        CarWish carWish = carWishRepository.findByUserAndCar(user, car);
        if (carWish != null) {
            carWishRepository.delete(carWish);
        }
    }

}
