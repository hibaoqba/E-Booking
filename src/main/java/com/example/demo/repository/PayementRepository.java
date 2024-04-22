package com.example.demo.repository;


import com.example.demo.model.Payement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PayementRepository extends JpaRepository<Payement,Long> {
    List<Payement> findByUserId(Integer userId);
}
