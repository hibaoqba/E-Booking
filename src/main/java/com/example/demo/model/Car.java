package com.example.demo.model;

import jakarta.persistence.*;
import jakarta.websocket.Encoder;
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
@Table(name="car")
public class Car {
    @Id
    @GeneratedValue
    private String id;
    private String brand;
    private String model;
    @Column(columnDefinition = "TEXT")
    private String description;
    private int year;
    @Enumerated(EnumType.STRING)
    private FuelType fuelType;
    @Enumerated(EnumType.STRING)
    private TransmissionType transmissionType;
    private int horsePower;
    private boolean availability;
    private int place;
    private int suitCases;
    private int price;
    @ElementCollection
    private List<String> images = new ArrayList<>();
    private boolean GPS;
    private boolean AC;

}
