package com.example.demo.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "apartment")
public class Apartment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titre;
    @Column(columnDefinition = "TEXT")
    private String description;
    private String city;
    private double price;
    @ElementCollection
    @CollectionTable(name = "apartment_images", joinColumns = @JoinColumn(name = "apartment_id"))
    @Column(name = "image", length = 1000000) // adjust length as per your requirement
    private List<String> images; // Storing multiple images as base64 strings
    @Embedded
    private ApartmentFeatures apartmentFeatures;
    @ManyToOne
    @JoinColumn(name = "seller_id")
    private User seller;
    /*@OneToMany(mappedBy = "apartment", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<AptReservation> aptReservations;*/
}