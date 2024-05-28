package com.example.demo.repository;

import com.example.demo.model.Car;
import com.example.demo.model.CarWish;
import com.example.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarWishRepository extends JpaRepository<CarWish, Long> {
    void deleteByUserId(Integer id);

    List<CarWish> findByUser(User user);
    CarWish findByUserAndCar(User user, Car car);
    void deleteCarWishByCarId(Long carId);
}
