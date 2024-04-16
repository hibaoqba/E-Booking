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
@Table(name="reservation")
public class Reservation {

    @Id
    @GeneratedValue
    private Long id;
    private LocalDate cmndDate;
    private LocalDate startDate;
    private String type;
    private String titre;
    private LocalDate endDate;
    private String status;
    private Integer days;
    private Integer Number;
    private double fraisSupp;
    private double initPrice;
    private double totalPrice;
    private double remainPrice;
    private Boolean childSeat;
    private Boolean infantSeat;
    private Boolean gps;
    private String paymentMethod;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="user_id")
    private User user;
    @ManyToOne(fetch = FetchType.EAGER)
    private Car car;
}
