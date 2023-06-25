package com.ti.acelera.plazoletamicroservice.adapters.driven.jpa.mysql.repositories;

import com.ti.acelera.plazoletamicroservice.adapters.driven.jpa.mysql.entity.DishEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IDishRepository extends JpaRepository<DishEntity,Long> {
    Page<DishEntity> findByRestaurantIdAndActiveTrue(Long restaurantId, Pageable pageable);
    Page<DishEntity> findByRestaurantIdAndCategoryIdAndActiveTrue(Long restaurantId, Long categoryId, Pageable pageable);
    List<DishEntity> findAllByIdInAndRestaurantIdAndActiveTrue(List<Long> dishIds, Long restaurantId);
    @Query("SELECT d.category, AVG(d.price) FROM DishEntity d WHERE d.restaurant.id = :restaurantId GROUP BY d.category")
    List<Object[]> categoryAveragePrice(@Param("restaurantId") Long restaurantId);
}
