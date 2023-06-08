package com.ti.acelera.plazoletamicroservice.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrderRestaurant {
    private Long id;
    private Long idClient;
    private LocalDate date;
    private String state;
    private Long idChef;
    private Restaurant restaurant;
    private List<DishOrder> dishes;
}
