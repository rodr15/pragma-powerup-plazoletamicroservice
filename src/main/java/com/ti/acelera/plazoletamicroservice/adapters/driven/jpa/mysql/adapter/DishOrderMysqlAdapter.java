package com.ti.acelera.plazoletamicroservice.adapters.driven.jpa.mysql.adapter;

import com.ti.acelera.plazoletamicroservice.adapters.driven.jpa.mysql.mappers.IDishOrderEntityMapper;
import com.ti.acelera.plazoletamicroservice.adapters.driven.jpa.mysql.repositories.IDishOrderRepository;
import com.ti.acelera.plazoletamicroservice.domain.model.DishOrder;
import com.ti.acelera.plazoletamicroservice.domain.spi.IDishOrderPersistencePort;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class DishOrderMysqlAdapter implements IDishOrderPersistencePort {

    private final IDishOrderRepository dishOrderRepository;
    private final IDishOrderEntityMapper dishOrderEntityMapper;

    @Override
    public List<DishOrder> getDishOrderByOrderRestaurantId(Long orderId) {
        return dishOrderRepository.findAllByOrderRestaurantId( orderId )
                .stream()
                .map( dishOrderEntityMapper::toDishOrder )
                .toList();
    }
}
