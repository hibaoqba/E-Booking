package com.example.demo.service;

import com.example.demo.model.Apartment;
import com.example.demo.model.Car;
import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ApartmentRepository apartmentRepository;

    @Autowired
    private CarService carService;
    @Autowired
    private ApartmentService apartmentService;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private CarWishRepository carWishRepository;

    @Autowired
    private ApartmentWishRepository apartmentWishRepository;

    @Autowired
    private SellerWaitRequestRepository sellerWaitRequestRepository;

    @Autowired
    private PayementRepository payementRepository;

    @Autowired
    private AptReservationRepository aptReservationRepository;


    public List<User> getAllusers() {
        return userRepository.findAll();
    }

    public Optional<User> getuserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> getuserById(Long id) {
        return userRepository.findById(String.valueOf(id));
    }

    public User saveuser(User user) {
        return userRepository.save(user);
    }

    public User updateUser(User user) {
        return userRepository.save(user);
    }
    public Long countAllUsers() {
        return userRepository.count();
    }

    public List<User> getAllCarSellers() {
        return userRepository.findByRole(Role.CARSELLER);
    }

    public List<User> getAllApartmentSellers() {
        return userRepository.findByRole(Role.APARTMENTSELLER);
    }

    public long countClients() {
        return userRepository.countClients();
    }


    public long countCarSellers() {
        return userRepository.countCarSellers();
    }

    public long countApartmentSellers() {
        return userRepository.countApartmentSellers();
    }
    public void deleteuser(Long id) {
        userRepository.deleteById(String.valueOf(id));
    }
    @Transactional
    public void deleteUser(Integer id) {
        // Delete payments associated with the user
        payementRepository.deleteByUserId(id);

        // Delete related tokens
        tokenRepository.deleteByUserId(id);

        // Delete related reservations
        reservationRepository.deleteByUserId(id);
        aptReservationRepository.deleteByUserId(id);

        // Delete related car wishes
        carWishRepository.deleteByUserId(id);

        // Delete related apartment wishes
        apartmentWishRepository.deleteByUserId(id);

        // Delete seller wait requests
        sellerWaitRequestRepository.deleteByUserId(id);

        // Find and delete all cars associated with the user
        List<Car> cars = carService.getSellerCars(id);
        for (Car car : cars) {
            carService.deleteCar(car.getId());
        }

        // Find and delete all apartments associated with the user
        List<Apartment> apartments = apartmentService.getSellerApartments(id);
        for (Apartment apartment : apartments) {
            apartmentRepository.deleteById(apartment.getId());
        }

        // Finally, delete the user
        userRepository.deleteUserById(id);
    }
}
