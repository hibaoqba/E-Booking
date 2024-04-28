package com.example.demo.repository;

import com.example.demo.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ApartmentWishRepository extends JpaRepository<ApartmentWish,Long> {
    List<ApartmentWish> findByUser(User user);
    ApartmentWish findByUserAndApartment(User user, Apartment apartment);
    void deleteApartmentWishByApartmentId(Long apartmentId);
}
