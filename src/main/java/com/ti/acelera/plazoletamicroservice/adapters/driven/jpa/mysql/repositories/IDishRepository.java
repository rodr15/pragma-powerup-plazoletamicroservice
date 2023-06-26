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
    @Query("SELECT d FROM DishEntity d WHERE price BETWEEN :lowBudget AND :upBudget AND d.category.id IN :preferences ORDER BY d.category.id ASC,d.restaurant.id ASC")
    Page<DishEntity> getDishesByBudgetAndCategoryPreferences(Long lowBudget,Long upBudget,List<Long> preferences,Pageable pageable);
    @Query("SELECT d FROM DishEntity d WHERE price BETWEEN :lowBudget AND :upBudget ORDER BY d.category.id ASC,d.restaurant.id ASC")
    Page<DishEntity> getDishesByBudget(Long lowBudget,Long upBudget,Pageable pageable);
}
