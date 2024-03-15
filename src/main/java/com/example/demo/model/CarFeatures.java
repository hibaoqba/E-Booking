package com.example.demo.model;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
@Embeddable
public class CarFeatures {
    @Enumerated(EnumType.STRING)
    private FuelType fuelType;
    @Enumerated(EnumType.STRING)
    private TransmissionType transmissionType;
    private int horsePower;
    private int place;
    private int suitCases;
    private boolean GPS;
    private boolean AC;
}
