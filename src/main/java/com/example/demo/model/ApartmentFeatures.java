package com.example.demo.model;

import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@Getter
@Setter
@Embeddable

public class ApartmentFeatures {
    private Integer noBed;
    private Integer noBathroom;
    private Integer square;
    private Integer minStayDays;
    private Integer noAdults;
    private Integer noChildren;
    private boolean airConditioning;
    private boolean breakfast;
    private boolean kitchen;
    private boolean parking;
    private boolean pool;
    private boolean wifiInternet;
}
