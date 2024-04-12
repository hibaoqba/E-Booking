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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String brand;
    private String model;
    @Column(columnDefinition = "TEXT")
    private String description;
    private int year;
    private String city;
    private boolean availability;
    private int price;
    @ElementCollection
    private List<String> images = new ArrayList<>();
    @Embedded
    private CarFeatures carFeatures;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    private User seller;

}
