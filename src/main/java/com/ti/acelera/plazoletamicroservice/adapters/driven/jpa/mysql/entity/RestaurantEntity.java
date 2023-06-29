package com.ti.acelera.plazoletamicroservice.adapters.driven.jpa.mysql.entity;

import com.ti.acelera.plazoletamicroservice.domain.model.RestaurantState;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "restaurant")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RestaurantEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String direction;
    private String idProprietary;
    private String phone;
    private String urlLogo;
    private String nit;
    private Set<String> employees;
    @Enumerated(EnumType.STRING)
    private RestaurantState state;
}
