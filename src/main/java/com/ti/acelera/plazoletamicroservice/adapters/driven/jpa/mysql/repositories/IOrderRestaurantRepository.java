package com.ti.acelera.plazoletamicroservice.adapters.driven.jpa.mysql.repositories;

import com.ti.acelera.plazoletamicroservice.adapters.driven.jpa.mysql.entity.OrderRestaurantEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IOrderRestaurantRepository extends JpaRepository<OrderRestaurantEntity,Long> {
    List<OrderRestaurantEntity> findByIdClientAndStateNot(Long idClient, String state);
    Page<OrderRestaurantEntity> findByRestaurantIdAndState(Long idClient, String state, Pageable pageable);
    List<OrderRestaurantEntity> findByRestaurantId(Long id);
    Optional<OrderRestaurantEntity> findById(Long id);
}
