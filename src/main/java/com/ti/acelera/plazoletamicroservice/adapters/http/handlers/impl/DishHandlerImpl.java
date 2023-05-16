package com.ti.acelera.plazoletamicroservice.adapters.http.handlers.impl;

import com.ti.acelera.plazoletamicroservice.adapters.http.dto.request.DishRequestDto;
import com.ti.acelera.plazoletamicroservice.adapters.http.dto.request.UpdateDishRequestDto;
import com.ti.acelera.plazoletamicroservice.adapters.http.handlers.IDishHandler;
import com.ti.acelera.plazoletamicroservice.adapters.http.mapper.IDishRequestMapper;
import com.ti.acelera.plazoletamicroservice.domain.api.IDishServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DishHandlerImpl implements IDishHandler {
     private final IDishServicePort dishServicePort;
     private final IDishRequestMapper restaurantRequestMapper;

    @Override
    public void saveDish(DishRequestDto dishRequestDto) {

        dishServicePort.saveDish( restaurantRequestMapper.toDish( dishRequestDto ) );
    }

    @Override
    public void modifyDish( Long dishId, UpdateDishRequestDto updateDishRequestDto) {
        dishServicePort.modifyDish(dishId, updateDishRequestDto.getPrice(), updateDishRequestDto.getDescription());
    }
}
