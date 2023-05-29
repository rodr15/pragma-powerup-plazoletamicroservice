package com.ti.acelera.plazoletamicroservice.adapters.http.handlers.impl;


import com.ti.acelera.plazoletamicroservice.adapters.http.dto.request.RestaurantRequestDto;
import com.ti.acelera.plazoletamicroservice.adapters.http.handlers.IRestaurantHandler;
import com.ti.acelera.plazoletamicroservice.adapters.http.mapper.IRestaurantRequestMapper;
import com.ti.acelera.plazoletamicroservice.domain.api.IRestaurantServicePort;
import com.ti.acelera.plazoletamicroservice.domain.model.Restaurant;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RestaurantHandlerImpl implements IRestaurantHandler {
    private final IRestaurantServicePort restaurantServicePort;
    private final IRestaurantRequestMapper restaurantRequestMapper;

    @Override
    public Page<RestaurantRequestDto> pageRestaurants(int page, int size) {
         Page<Restaurant> restaurantPage = restaurantServicePort.pageRestaurants(page,size);
        return restaurantPage.map( restaurantRequestMapper::toRequestDto );
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
