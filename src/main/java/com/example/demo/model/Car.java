package com.example.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private int year;
    private String fuel;
    private String gear;
    private boolean availability;
    private int place;
    private int bagage;
    private int price;



}
