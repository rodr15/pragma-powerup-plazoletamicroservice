package com.ti.acelera.plazoletamicroservice.adapters.driven.jpa.mysql.repositories;

import com.ti.acelera.plazoletamicroservice.adapters.driven.jpa.mysql.entity.OrderRestaurantEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IOrderRestaurantRepository extends JpaRepository<OrderRestaurantEntity,Long> {
    List<OrderRestaurantEntity> findByIdClientAndStateNot(Long idClient, String state);
}
