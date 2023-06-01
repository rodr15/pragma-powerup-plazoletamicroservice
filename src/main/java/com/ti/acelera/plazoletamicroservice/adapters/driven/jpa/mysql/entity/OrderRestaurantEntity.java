package com.ti.acelera.plazoletamicroservice.adapters.driven.jpa.mysql.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "orderRestaurant")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrderRestaurantEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long idClient;
    private Date date;
    private String state;
    private Long idChef;
    @ManyToOne
    @JoinColumn(name = "id_restaurant")
    private RestaurantEntity restaurant;

    @Override
    public String toString() {
        return "OrderRestaurantEntity{" +
                "id=" + id +
                ", idClient=" + idClient +
                ", date=" + date +
                ", state='" + state + '\'' +
                ", idChef=" + idChef +
                ", restaurant=" + restaurant +
                '}';
    }

}
