package com.ti.acelera.plazoletamicroservice.adapters.driven.jpa.mysql.adapter;

import com.ti.acelera.plazoletamicroservice.adapters.driven.jpa.mysql.entity.DishOrderEntity;
import com.ti.acelera.plazoletamicroservice.adapters.driven.jpa.mysql.entity.OrderRestaurantEntity;
import com.ti.acelera.plazoletamicroservice.adapters.driven.jpa.mysql.mappers.IDishOrderEntityMapper;
import com.ti.acelera.plazoletamicroservice.adapters.driven.jpa.mysql.mappers.IOrderEntityMapper;
import com.ti.acelera.plazoletamicroservice.adapters.driven.jpa.mysql.repositories.IDishOrderRepository;
import com.ti.acelera.plazoletamicroservice.adapters.driven.jpa.mysql.repositories.IOrderRestaurantRepository;
import com.ti.acelera.plazoletamicroservice.domain.model.OrderRestaurant;
import com.ti.acelera.plazoletamicroservice.domain.model.OrderStatus;
import com.ti.acelera.plazoletamicroservice.domain.spi.IOrderRestaurantPersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class OrderRestaurantMysqlAdapter implements IOrderRestaurantPersistencePort {

    private final IOrderRestaurantRepository orderRestaurantRepository;
    private final IOrderEntityMapper orderEntityMapper;
    private final IDishOrderRepository dishOrderRepository;
    private final IDishOrderEntityMapper dishOrderEntityMapper;


    @Override
    public Optional<List<OrderRestaurant>> getOrdersById(List<Long> ordersId) {
        List<OrderRestaurantEntity> list = orderRestaurantRepository.findAllById(ordersId);

        if (list.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(list.stream()
                .map(orderEntityMapper::toOrder)
                .toList());

    }

    @Override
    public List<OrderRestaurant> saveAllOrderRestaurant(List<OrderRestaurant> orderRestaurant) {
        List<OrderRestaurantEntity> orderRestaurantEntityList = orderRestaurant
                .stream()
                .map(orderEntityMapper::toOrderEntity)
                .toList();

        return orderRestaurantRepository.saveAll(orderRestaurantEntityList)
                .stream()
                .map(orderEntityMapper::toOrder)
                .toList();
    }

    @Override
    public Page<OrderRestaurant> getOrdersList(Long restaurantId, OrderStatus state, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return orderRestaurantRepository.findByRestaurantIdAndOrderStatus(restaurantId, state, pageable).map(orderEntityMapper::toOrder);
    }

    @Override
    public List<OrderRestaurant> getOrdersList(Long restaurantId) {
        return  orderRestaurantRepository.findByRestaurantId( restaurantId )
                .stream()
                .map( orderEntityMapper::toOrder )
                .toList();
    }

    @Override
    public List<OrderRestaurant> getOrdersListWithStatus(Long restaurantId, OrderStatus orderStatus) {
        return orderRestaurantRepository.findByRestaurantIdAndOrderStatus(restaurantId,orderStatus)
                .stream()
                .map(orderEntityMapper::toOrder)
                .toList();
    }

    @Override
    public boolean clientHasUnfinishedOrders(Long clientId) {
        List<OrderRestaurantEntity> unfinishedOrders  = orderRestaurantRepository.findByIdClientAndOrderStatusNotIn( clientId, List.of(OrderStatus.FINISHED_ORDER , OrderStatus.CANCELED_ORDER) );
        return !unfinishedOrders.isEmpty();
    }

    @Override
    public OrderRestaurant createNewOrder(OrderRestaurant orderRestaurant) {
        OrderRestaurantEntity savedOrderRestaurantEntity = orderRestaurantRepository.save(orderEntityMapper.toOrderEntity(orderRestaurant));
        OrderRestaurant savedOrderRestaurant = orderEntityMapper.toOrder(savedOrderRestaurantEntity);
        orderRestaurant.getDishes().forEach(
                dishOrder -> {
                    dishOrder.setOrder(savedOrderRestaurant);
                    DishOrderEntity dishOrderEntity = dishOrderEntityMapper.toDishOrderEntity(dishOrder);
                    dishOrderRepository.save(dishOrderEntity);
                }
        );

        return savedOrderRestaurant;
    }

    @Override
    public Optional<OrderRestaurant> getOrderById(Long orderId) {
        return orderRestaurantRepository.findById( orderId ).map( orderEntityMapper::toOrder );
    }
    
    @Override
    public OrderRestaurant saveOrderRestaurant(OrderRestaurant orderRestaurant) {
        OrderRestaurantEntity savedOrderRestaurant =  orderRestaurantRepository.save( orderEntityMapper.toOrderEntity(orderRestaurant) );
        return orderEntityMapper.toOrder(savedOrderRestaurant);
    }

    @Override
    public boolean restaurantHasUnfinishedOrders(Long restaurantId) {
        List<OrderStatus> statusList = List.of(OrderStatus.FINISHED_ORDER , OrderStatus.CANCELED_ORDER);
        return orderRestaurantRepository.existsOrderByRestaurantIdAndStatusNotIn( restaurantId, statusList );
    }

    @Override
    public void deleteAllOrderRestaurant(List<OrderRestaurant> orderRestaurantList) {
        orderRestaurantRepository.deleteAll( orderRestaurantList.stream().map(orderEntityMapper::toOrderEntity).toList() );
    }
}
