package com.ti.acelera.plazoletamicroservice.adapters.driven.jpa.mysql.repositories;

import com.ti.acelera.plazoletamicroservice.adapters.driven.jpa.mysql.entity.DishOrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IDishOrderRepository extends JpaRepository<DishOrderEntity,Long> {
    List<DishOrderEntity> findAllByOrderRestaurantId(Long idOrder );
}
