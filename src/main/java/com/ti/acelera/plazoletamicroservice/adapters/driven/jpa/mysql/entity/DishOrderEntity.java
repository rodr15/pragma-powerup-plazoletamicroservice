package com.ti.acelera.plazoletamicroservice.adapters.driven.jpa.mysql.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "dishOrder")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DishOrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "id_order")
    private OrderRestaurantEntity orderRestaurant;
    @ManyToOne
    @JoinColumn(name = "id_dish")
    private DishEntity dish;
    private int quantity;
}
