package com.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="apt_reservation")
public class AptReservation {
    @Id
    @GeneratedValue
    private Long id;
    private LocalDate cmndDate;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;
    private String titre;
    private String type;
    private Integer days;
    private Integer noAdults;
    private Integer noChilds;
    private double fraisSupp;
    private double initPrice;
    private double totalPrice;
    private double remainPrice;
    private Boolean lawnGarden;
    private Boolean clearning;
    private Boolean breakfasts;
    private String paymentMethod;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="user_id")
    private User user;
    @ManyToOne(fetch = FetchType.EAGER)
    private Apartment apartment;

}
