package com.ti.acelera.plazoletamicroservice.adapters.driven.jpa.mysql.repositories;


import com.ti.acelera.plazoletamicroservice.adapters.driven.jpa.mysql.entity.RestaurantEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IRestaurantRepository extends JpaRepository<RestaurantEntity,Long> {
}
