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
    private List<String> images = new ArrayList<>();
    @Embedded
    private InfoSupplim infoSupplim;
    @ElementCollection
    private List<String> amenities = new ArrayList<>();
    @ManyToOne
    @JoinColumn(name = "seller_id")
    private User seller;
    @OneToMany(mappedBy = "apartment", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<AptReservation> aptReservations;
}
