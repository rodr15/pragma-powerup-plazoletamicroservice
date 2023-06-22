package com.ti.acelera.plazoletamicroservice.adapters.driven.jpa.mysql.repositories;

import com.ti.acelera.plazoletamicroservice.adapters.driven.jpa.mysql.entity.OrderRestaurantEntity;
import com.ti.acelera.plazoletamicroservice.domain.model.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IOrderRestaurantRepository extends JpaRepository<OrderRestaurantEntity,Long> {
    List<OrderRestaurantEntity> findByIdClientAndOrderStatusNot(Long idClient, OrderStatus status);
    List<OrderRestaurantEntity> findByIdClientAndOrderStatusNotIn(Long clientId, List<OrderStatus> excludedStatus);
    Page<OrderRestaurantEntity> findByRestaurantIdAndOrderStatus(Long restaurantId, OrderStatus status, Pageable pageable);
    List<OrderRestaurantEntity> findByRestaurantIdAndOrderStatus(Long restaurantId, OrderStatus status);
    List<OrderRestaurantEntity> findByRestaurantId(Long id);
    Optional<OrderRestaurantEntity> findById(Long id);
}
