package com.ti.acelera.plazoletamicroservice.adapters.http.handlers.impl;


import com.ti.acelera.plazoletamicroservice.adapters.http.dto.request.OrderRequestDto;
import com.ti.acelera.plazoletamicroservice.adapters.http.dto.request.RestaurantRequestDto;
import com.ti.acelera.plazoletamicroservice.adapters.http.dto.response.DishResponseDto;
import com.ti.acelera.plazoletamicroservice.adapters.http.dto.response.OrderRestaurantResponseDto;
import com.ti.acelera.plazoletamicroservice.adapters.http.dto.response.RestaurantResponseDto;
import com.ti.acelera.plazoletamicroservice.adapters.http.handlers.IRestaurantHandler;
import com.ti.acelera.plazoletamicroservice.adapters.http.mapper.*;
import com.ti.acelera.plazoletamicroservice.domain.api.IRestaurantServicePort;
import com.ti.acelera.plazoletamicroservice.domain.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantHandlerImpl implements IRestaurantHandler {
    private final IRestaurantServicePort restaurantServicePort;
    private final IRestaurantRequestMapper restaurantRequestMapper;
    private final IRestaurantResponseMapper restaurantResponseMapper;
    private final IDishResponseMapper dishResponseMapper;
    private final IOrderRestaurantRequestMapper orderRestaurantRequestMapper;
    private final IOrderRestaurantResponseMapper orderRestaurantResponseMapper;

    @Override
    public void assignEmployeeToOrders(String employeeId, List<Long> ordersId) {
        restaurantServicePort.assignEmployeeToOrder( employeeId,ordersId );
    }

    @Override
    public Page<OrderRestaurantResponseDto> getOrdersListByEmployeeId(Long employeeId, OrderStatus state, int page, int size) {
        Page<OrderRestaurant> orderRestaurantPage = restaurantServicePort.getOrdersPage(employeeId, state, page, size);
        return orderRestaurantPage.map(orderRestaurantResponseMapper::toOrderRestaurantResponseDto);
    }

    @Override
    public void makeOrder(Long clientId, OrderRequestDto orderRequestDto) {
        OrderRestaurant orderRestaurant = orderRestaurantRequestMapper.toOrderRestaurant(orderRequestDto);
        orderRestaurant.setIdClient(clientId);
        restaurantServicePort.makeOrder(orderRestaurant);
    }

    @Override
    public Page<DishResponseDto> pageDishes(Long restaurantId, Long categoryId, int page, int size) {
        Page<Dish> dishPage = restaurantServicePort.pageDish(restaurantId, categoryId, page, size);
        return dishPage.map(dishResponseMapper::toResponseDto);
    }

    @Override
    public Page<RestaurantResponseDto> pageRestaurants(int page, int size) {
        Page<Restaurant> restaurantPage = restaurantServicePort.pageRestaurants(page, size);
        return restaurantPage.map(restaurantResponseMapper::toResponseDto);
    }

    @Override
    public void saveRestaurant(RestaurantRequestDto restaurantRequestDto) {
        restaurantServicePort.saveRestaurant(restaurantRequestMapper.toRestaurant(restaurantRequestDto));
    }

    @Override
    public void assignRestaurantEmployee(String employeeId, Long restaurantId) {
        restaurantServicePort.assignEmployeeToRestaurant(employeeId, restaurantId);
    }

    public boolean verifyRestaurantOwner(String userId, Long restaurantId) {
        return restaurantServicePort.verifyRestaurantOwner(userId, restaurantId);
    }

    @Override
    public void finishRestaurantOrder(Long orderRestaurantId) {
        restaurantServicePort.orderRestaurantReady( orderRestaurantId );
    }

    @Override
    public void deliverRestaurantOrder(Long orderRestaurantId, String verificationCode, Long employeeId) {
        restaurantServicePort.orderRestaurantDeliver( orderRestaurantId ,verificationCode,employeeId);
    }

    @Override
    public void cancelOrder(Long userId,Long orderId) {
        restaurantServicePort.cancelOrder( userId,orderId  );
    }

    @Override
    public List<Traceability> historyOrder(Long userId, Long orderId) {
        return restaurantServicePort.historyOrder(userId,orderId);
    }

    @Override
    public RestaurantStatistics restaurantStatistics(Long userId,Long restaurantId) {
        return restaurantServicePort.restaurantStatistics( userId, restaurantId );
    }

    @Override
    public List<CategoryAveragePrice> dishCategoryAveragePrice(Long userId, Long restaurantId) {
        return restaurantServicePort.dishCategoryAveragePrice(userId,restaurantId);
    }

    @Override
    public void deleteRestaurant(Long userId, Long restaurantId) {
        restaurantServicePort.requestDeleteRestaurant(userId,restaurantId);
    }

}
