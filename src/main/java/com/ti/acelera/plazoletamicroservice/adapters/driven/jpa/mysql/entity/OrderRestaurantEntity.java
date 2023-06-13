package com.ti.acelera.plazoletamicroservice.adapters.driven.jpa.mysql.entity;

import com.ti.acelera.plazoletamicroservice.domain.model.OrderStatus;
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
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
    private Long idChef;
    @ManyToOne
    @JoinColumn(name = "id_restaurant")
    private RestaurantEntity restaurant;
    private String verificationCode;
    @Override
    public String toString() {
        return "OrderRestaurantEntity{" +
                "id=" + id +
                ", idClient=" + idClient +
                ", date=" + date +
                ", state='" + orderStatus + '\'' +
                ", idChef=" + idChef +
                ", restaurant=" + restaurant +
                '}';
    }

}
