package com.ti.acelera.plazoletamicroservice.adapters.driven.jpa.mysql.repositories;

import com.ti.acelera.plazoletamicroservice.adapters.driven.jpa.mysql.entity.OrderRestaurantEntity;
import com.ti.acelera.plazoletamicroservice.domain.model.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IOrderRestaurantRepository extends JpaRepository<OrderRestaurantEntity,Long> {
    List<OrderRestaurantEntity> findByIdClientAndOrderStatusNot(Long idClient, OrderStatus status);
    List<OrderRestaurantEntity> findByIdClientAndOrderStatusNotIn(Long clientId, List<OrderStatus> excludedStatus);
    Page<OrderRestaurantEntity> findByRestaurantIdAndOrderStatus(Long restaurantId, OrderStatus status, Pageable pageable);
    List<OrderRestaurantEntity> findByRestaurantIdAndOrderStatus(Long restaurantId, OrderStatus status);
    List<OrderRestaurantEntity> findByRestaurantId(Long id);
    Optional<OrderRestaurantEntity> findById(Long id);
    @Query("SELECT CASE WHEN COUNT(o) > 0 THEN true ELSE false END FROM OrderRestaurantEntity o WHERE o.restaurant.id = :restaurantId AND o.orderStatus NOT IN :excludedStatus")
    boolean existsOrderByRestaurantIdAndStatusNotIn(@Param("restaurantId") Long restaurantId, @Param("excludedStatus") List<OrderStatus> excludedStatus);

}
