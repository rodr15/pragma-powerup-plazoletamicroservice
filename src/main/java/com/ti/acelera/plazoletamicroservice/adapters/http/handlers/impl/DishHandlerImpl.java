package com.ti.acelera.plazoletamicroservice.adapters.http.handlers.impl;

import com.ti.acelera.plazoletamicroservice.adapters.http.dto.request.DishRequestDto;
import com.ti.acelera.plazoletamicroservice.adapters.http.handlers.IDishHandler;
import com.ti.acelera.plazoletamicroservice.adapters.http.mapper.IDishRequestMapper;
import com.ti.acelera.plazoletamicroservice.domain.api.IDishServicePort;

public class DishHandlerImpl implements IDishHandler {
     private final IDishServicePort dishServicePort;
     private final IDishRequestMapper restaurantRequestMapper;

    public DishHandlerImpl(IDishServicePort dishServicePort,IDishRequestMapper restaurantRequestMapper) {
        this.dishServicePort = dishServicePort;
        this.restaurantRequestMapper = restaurantRequestMapper;
    }

    @Override
    public void saveDish(DishRequestDto dishRequestDto) {

        dishServicePort.saveDish( restaurantRequestMapper.toDish( dishRequestDto ) );
    }
}
