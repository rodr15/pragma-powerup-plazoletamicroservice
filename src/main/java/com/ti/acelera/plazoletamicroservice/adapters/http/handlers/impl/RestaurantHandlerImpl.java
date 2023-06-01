package com.ti.acelera.plazoletamicroservice.adapters.http.handlers.impl;


import com.ti.acelera.plazoletamicroservice.adapters.http.dto.request.OrderRequestDto;
import com.ti.acelera.plazoletamicroservice.adapters.http.dto.request.RestaurantRequestDto;
import com.ti.acelera.plazoletamicroservice.adapters.http.dto.response.DishResponseDto;
import com.ti.acelera.plazoletamicroservice.adapters.http.dto.response.RestaurantResponseDto;
import com.ti.acelera.plazoletamicroservice.adapters.http.handlers.IRestaurantHandler;
import com.ti.acelera.plazoletamicroservice.adapters.http.mapper.IDishResponseMapper;
import com.ti.acelera.plazoletamicroservice.adapters.http.mapper.IOrderRestaurantRequestMapper;
import com.ti.acelera.plazoletamicroservice.adapters.http.mapper.IRestaurantRequestMapper;
import com.ti.acelera.plazoletamicroservice.adapters.http.mapper.IRestaurantResponseMapper;
import com.ti.acelera.plazoletamicroservice.domain.api.IRestaurantServicePort;
import com.ti.acelera.plazoletamicroservice.domain.model.Dish;
import com.ti.acelera.plazoletamicroservice.domain.model.OrderRestaurant;
import com.ti.acelera.plazoletamicroservice.domain.model.Restaurant;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RestaurantHandlerImpl implements IRestaurantHandler {
    private final IRestaurantServicePort restaurantServicePort;
    private final IRestaurantRequestMapper restaurantRequestMapper;
    private final IRestaurantResponseMapper restaurantResponseMapper;
    private final IDishResponseMapper dishResponseMapper;
    private final IOrderRestaurantRequestMapper orderRestaurantRequestMapper;

    @Override
    public void makeOrder(Long clientId,OrderRequestDto orderRequestDto) {
        OrderRestaurant orderRestaurant =  orderRestaurantRequestMapper.toOrderRestaurant( orderRequestDto );
        orderRestaurant.setIdClient( clientId );
        restaurantServicePort.makeOrder( orderRestaurant );
    }

    @Override
    public Page<DishResponseDto> pageDishes(Long restaurantId, Long categoryId , int page, int size) {
       Page<Dish> dishPage = restaurantServicePort.pageDish( restaurantId,categoryId,page,size );
        return dishPage.map( dishResponseMapper :: toResponseDto );
    }

    @Override
    public Page<RestaurantResponseDto> pageRestaurants(int page, int size) {
         Page<Restaurant> restaurantPage = restaurantServicePort.pageRestaurants(page,size);
        return restaurantPage.map( restaurantResponseMapper::toResponseDto );
    }

    @Override
    public void saveRestaurant(RestaurantRequestDto restaurantRequestDto) {
        restaurantServicePort.saveRestaurant( restaurantRequestMapper.toRestaurant( restaurantRequestDto) );
    }

    @Override
    public void assignRestaurantEmployee(String employeeId, Long restaurantId) {
        restaurantServicePort.assignEmployee( employeeId, restaurantId );
    }

    public boolean verifyRestaurantOwner(String userId, Long restaurantId) {
        return restaurantServicePort.verifyRestaurantOwner( userId , restaurantId );
    }

}
