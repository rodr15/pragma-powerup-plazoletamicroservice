package com.ti.acelera.plazoletamicroservice.adapters.driven.jpa.mysql.adapter;

import com.ti.acelera.plazoletamicroservice.adapters.driven.jpa.mysql.entity.DishOrderEntity;
import com.ti.acelera.plazoletamicroservice.adapters.driven.jpa.mysql.entity.OrderRestaurantEntity;
import com.ti.acelera.plazoletamicroservice.adapters.driven.jpa.mysql.mappers.IDishOrderEntityMapper;
import com.ti.acelera.plazoletamicroservice.adapters.driven.jpa.mysql.mappers.IOrderEntityMapper;
import com.ti.acelera.plazoletamicroservice.adapters.driven.jpa.mysql.repositories.IDishOrderRepository;
import com.ti.acelera.plazoletamicroservice.adapters.driven.jpa.mysql.repositories.IOrderRestaurantRepository;
import com.ti.acelera.plazoletamicroservice.domain.model.OrderRestaurant;
import com.ti.acelera.plazoletamicroservice.domain.spi.IOrderRestaurantPersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.ti.acelera.plazoletamicroservice.configuration.Constants.FINISHED_ORDER;

@RequiredArgsConstructor
public class OrderRestaurantMysqlAdapter implements IOrderRestaurantPersistencePort {

    private final IOrderRestaurantRepository orderRestaurantRepository;
    private final IOrderEntityMapper orderEntityMapper;
    private final IDishOrderRepository dishOrderRepository;
    private final IDishOrderEntityMapper dishOrderEntityMapper;

    @Override
    public Page<OrderRestaurant> getOrdersList(Long restaurantId, String state, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return orderRestaurantRepository.findByRestaurantIdAndState(restaurantId, state, pageable).map(orderEntityMapper::toOrder);
    }

    @Override
    public boolean hasUnfinishedOrders(Long clientId) {
        List<OrderRestaurantEntity> unfinishedOrders = orderRestaurantRepository.findByIdClientAndStateNot(clientId, FINISHED_ORDER);
        return !unfinishedOrders.isEmpty();
    }

    @Override
    public Long createNewOrder(OrderRestaurant orderRestaurant) {
        OrderRestaurantEntity savedOrderRestraurantEntity = orderRestaurantRepository.save(orderEntityMapper.toOrderEntity(orderRestaurant));
        OrderRestaurant savedOrderRestraurant = orderEntityMapper.toOrder(savedOrderRestraurantEntity);
        orderRestaurant.getDishes().forEach(
                dishOrder -> {
                    dishOrder.setOrder(savedOrderRestraurant);
                    DishOrderEntity dishOrderEntity = dishOrderEntityMapper.toDishOrderEntity(dishOrder);
                    dishOrderRepository.save(dishOrderEntity);
                }
        );

        return savedOrderRestraurant.getId();
    }
}
