package com.ti.acelera.plazoletamicroservice.adapters.http.handlers;

import com.ti.acelera.plazoletamicroservice.adapters.http.dto.request.DishRequestDto;
import com.ti.acelera.plazoletamicroservice.adapters.http.dto.request.UpdateDishRequestDto;

public interface IDishHandler {
    void saveDish(DishRequestDto dishRequestDto);
    void modifyDish(Long dishId, UpdateDishRequestDto updateDishRequestDto );
}
