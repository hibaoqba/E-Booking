package com.example.demo.controller;

import com.example.demo.model.Role;
import com.example.demo.model.SellerWaitRequest;
import com.example.demo.model.User;
import com.example.demo.repository.SellerWaitRequestRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.SellerWaitRequestService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/seller/waitrequests")
public class SellerWaitRequestController {

    @Autowired
    private UserService userService;
    @Autowired
    private SellerWaitRequestRepository sellerWaitRequestRepository;
    private final SellerWaitRequestService sellerWaitRequestService;

    @Autowired
    public SellerWaitRequestController(SellerWaitRequestService sellerWaitRequestService) {
        this.sellerWaitRequestService = sellerWaitRequestService;
    }

    @GetMapping
    public ResponseEntity<List<SellerWaitRequest>> getAllSellerWaitRequests() {
        List<SellerWaitRequest> sellerWaitRequests = sellerWaitRequestService.getAllSellerWaitRequests();
        return ResponseEntity.ok(sellerWaitRequests);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SellerWaitRequest> getSellerWaitRequestById(@PathVariable("id") Long id) {
        Optional<SellerWaitRequest> sellerWaitRequest = sellerWaitRequestService.getSellerWaitRequestById(id);
        return sellerWaitRequest.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createSellerWaitRequest(@RequestBody SellerWaitRequest sellerWaitRequest) {
        SellerWaitRequest createdSellerWaitRequest = sellerWaitRequestService.createSellerWaitRequest(sellerWaitRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body("request sent");
    }

    @PutMapping("/{id}")
    public ResponseEntity<SellerWaitRequest> updateSellerWaitRequest(@PathVariable("id") Long id, @RequestBody SellerWaitRequest sellerWaitRequest) {
        SellerWaitRequest updatedSellerWaitRequest = sellerWaitRequestService.updateSellerWaitRequest(id, sellerWaitRequest);
        if (updatedSellerWaitRequest != null) {
            return ResponseEntity.ok(updatedSellerWaitRequest);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/treat/{id}")
    public ResponseEntity<SellerWaitRequest> treatSellerRequest(@PathVariable("id") Long id) {
        SellerWaitRequest sellerWaitRequest = sellerWaitRequestService.treatSellerReuqest(id);
        if (sellerWaitRequest == null) {
            return ResponseEntity.notFound().build();
        }
        User user = sellerWaitRequest.getUser();
        if (user == null) {
            // Handle the case where the user associated with the request is not found
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        user.setRole(Role.valueOf(sellerWaitRequest.getRole()));
        userService.updateUser(user);
        return ResponseEntity.ok(sellerWaitRequest);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSellerWaitRequest(@PathVariable("id") Long id) {
        sellerWaitRequestService.deleteSellerWaitRequest(id);
        return ResponseEntity.noContent().build();
    }
}
