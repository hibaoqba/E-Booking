package com.example.demo.repository;

import com.example.demo.model.SellerWaitRequest;
import com.example.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SellerWaitRequestRepository extends JpaRepository<SellerWaitRequest,Long> {
    void deleteByUserId(Integer id);

}
