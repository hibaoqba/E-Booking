package com.example.demo.service;

import com.example.demo.model.*;
import com.example.demo.repository.ApartmentWishRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApartmentWishService {
    @Autowired
    private ApartmentWishRepository apartmentWishRepository;
    public List<ApartmentWish> getAllWishesByUser(User user) {
        return apartmentWishRepository.findByUser(user);
    }

    public void addWish(User user, Apartment apartment) {
        ApartmentWish apartmentWish = new ApartmentWish();
        apartmentWish.setUser(user);
        apartmentWish.setApartment(apartment);
        apartmentWishRepository.save(apartmentWish);
    }

    public void removeWish(User user, Apartment apartment) {
        ApartmentWish apartmentWish = apartmentWishRepository.findByUserAndApartment(user, apartment);
        if (apartmentWish != null) {
            apartmentWishRepository.delete(apartmentWish);
        }
    }
}
