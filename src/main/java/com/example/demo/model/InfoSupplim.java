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

public class InfoSupplim {
    private Integer noBed;
    private Integer noBathroom;
    private Integer square;
    private Integer minStayDays;
}
