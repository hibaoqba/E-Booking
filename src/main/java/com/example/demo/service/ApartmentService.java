package com.example.demo.service;

import com.example.demo.model.Apartment;
import com.example.demo.model.Car;
import com.example.demo.repository.ApartmentRepository;
import com.example.demo.repository.ApartmentWishRepository;
import com.example.demo.repository.AptReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ApartmentService {
    @Autowired
    private ApartmentWishRepository apartmentWishRepository;

    @Autowired
    private AptReservationRepository aptReservationRepository;

    private final ApartmentRepository apartmentRepository;

    @Autowired
    public ApartmentService(ApartmentRepository apartmentRepository) {
        this.apartmentRepository = apartmentRepository;
    }

    public List<Apartment> getAllApartments() {
        return apartmentRepository.findAll();
    }

    public Long countApartmentsBySellerId(Integer sellerId) {
        return apartmentRepository.countApartmentBySellerId(sellerId);
    }


    public List<Apartment> getSellerApartments(Integer sellerId) {
        return apartmentRepository.findApartmentsBySellerId(sellerId);
    }

    

    public Optional<Apartment> getApartmentById(Long id) {
        return apartmentRepository.findById(id);
    }

    public Apartment saveApartment(Apartment apartment) {
        return apartmentRepository.save(apartment);
    }

    public void deleteApartment(Long id) {

        aptReservationRepository.deleteReservationsByApartmentId(id);
        apartmentWishRepository.deleteApartmentWishByApartmentId(id);
        apartmentRepository.deleteById(id);
    }
}
