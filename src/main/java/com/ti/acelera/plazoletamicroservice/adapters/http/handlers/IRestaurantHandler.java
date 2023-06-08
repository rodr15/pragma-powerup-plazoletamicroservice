package com.ti.acelera.plazoletamicroservice.adapters.http.handlers;


import com.ti.acelera.plazoletamicroservice.adapters.http.dto.request.OrderRequestDto;
import com.ti.acelera.plazoletamicroservice.adapters.http.dto.request.RestaurantRequestDto;
import com.ti.acelera.plazoletamicroservice.adapters.http.dto.response.DishResponseDto;
import com.ti.acelera.plazoletamicroservice.adapters.http.dto.response.OrderRestaurantResponseDto;
import com.ti.acelera.plazoletamicroservice.adapters.http.dto.response.RestaurantResponseDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IRestaurantHandler {
    void assignEmployeeToOrders(String employeeId , List<Long> ordersId);
    Page<OrderRestaurantResponseDto> getOrdersListByEmployeeId(Long employeeId, String state, int page, int size );
    void makeOrder(Long clientId,OrderRequestDto orderRequestDto) ;
    Page<DishResponseDto> pageDishes(Long restaurantId, Long categoryId , int page, int size);
    Page<RestaurantResponseDto> pageRestaurants(int page, int size);
    void saveRestaurant(RestaurantRequestDto restaurantRequestDto);
    void assignRestaurantEmployee(String employeeId , Long restaurantId);
    boolean verifyRestaurantOwner( String userid,  Long restaurantId );

}
