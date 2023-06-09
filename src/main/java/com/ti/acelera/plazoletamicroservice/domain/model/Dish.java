package com.ti.acelera.plazoletamicroservice.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Dish {
    private Long id;
    private String name;
    private Category category;
    private String description;
    private Long price;
    private Restaurant restaurant;
    private String urlImage;
    private boolean active;

}
