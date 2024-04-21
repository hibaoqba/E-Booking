package com.example.demo.service;

import com.example.demo.model.SellerWaitRequest;
import com.example.demo.repository.SellerWaitRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class SellerWaitRequestService {


    private final SellerWaitRequestRepository sellerWaitRequestRepository;

    @Autowired
    public SellerWaitRequestService(SellerWaitRequestRepository sellerWaitRequestRepository) {
        this.sellerWaitRequestRepository = sellerWaitRequestRepository;
    }

    public List<SellerWaitRequest> getAllSellerWaitRequests() {
        return sellerWaitRequestRepository.findAll();
    }

    public Optional<SellerWaitRequest> getSellerWaitRequestById(Long id) {
        return sellerWaitRequestRepository.findById(id);
    }

    public SellerWaitRequest createSellerWaitRequest(SellerWaitRequest sellerWaitRequest) {
        sellerWaitRequest.setStatus("pending");
        sellerWaitRequest.setRequestDate(LocalDate.now());
        return sellerWaitRequestRepository.save(sellerWaitRequest);
    }

    public SellerWaitRequest treatSellerReuqest(Long sellerWaitRequestId) {
        Optional<SellerWaitRequest> sellerWaitRequestOptional = sellerWaitRequestRepository.findById(sellerWaitRequestId);
            SellerWaitRequest sellerWaitRequest = sellerWaitRequestOptional.get();
            sellerWaitRequest.setStatus("treated");
            return sellerWaitRequestRepository.save(sellerWaitRequest);
    }

    public SellerWaitRequest updateSellerWaitRequest(Long id, SellerWaitRequest sellerWaitRequest) {
        if (sellerWaitRequestRepository.existsById(id)) {
            sellerWaitRequest.setId(id);
            return sellerWaitRequestRepository.save(sellerWaitRequest);
        } else {
            return null; // or throw an exception indicating that the entity does not exist
        }
    }

    public void deleteSellerWaitRequest(Long id) {
        sellerWaitRequestRepository.deleteById(id);
    }
}
