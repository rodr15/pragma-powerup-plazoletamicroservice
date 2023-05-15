package com.ti.acelera.plazoletamicroservice.adapters.http.handlers;

import com.ti.acelera.plazoletamicroservice.adapters.http.dto.request.DishRequestDto;

public interface IDishHandler {
    void saveDish(DishRequestDto dishRequestDto);
}
