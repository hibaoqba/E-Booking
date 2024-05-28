package com.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "car")
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String brand;
    private String model;
    @Column(columnDefinition = "TEXT")
    private String description;
    private int year;
    private double latitude;
    private double longitude;
    private boolean availability;
    private String address;
    private String city;
    private double price;
    @ElementCollection
    @CollectionTable(name = "car_images", joinColumns = @JoinColumn(name = "car_id"))
    @Column(name = "image", length = 1000000) // adjust length as per your requirement
    private List<String> images;
    @Embedded
    private CarFeatures carFeatures;
    @ManyToOne
    @JoinColumn(name = "seller_id")
    private User seller;

}
