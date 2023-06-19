package com.ti.acelera.plazoletamicroservice.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrderRestaurant {
    private Long id;
    private Long idClient;
    private LocalDateTime date;
    private OrderStatus orderStatus;
    private Long idChef;
    private Restaurant restaurant;
    private List<DishOrder> dishes;
    private String verificationCode;
}
