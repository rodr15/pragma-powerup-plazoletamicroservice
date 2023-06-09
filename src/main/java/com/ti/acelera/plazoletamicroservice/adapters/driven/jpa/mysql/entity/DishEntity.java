package com.ti.acelera.plazoletamicroservice.adapters.driven.jpa.mysql.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "dish")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DishEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private Long price;
    private String urlImage;
    private boolean active;
    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private RestaurantEntity restaurant;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private CategoryEntity category;
}
