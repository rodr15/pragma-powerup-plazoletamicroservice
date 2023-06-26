package com.ti.acelera.plazoletamicroservice.adapters.http.handlers;

import com.ti.acelera.plazoletamicroservice.adapters.http.dto.request.DishRequestDto;
import com.ti.acelera.plazoletamicroservice.adapters.http.dto.request.UpdateDishRequestDto;
import com.ti.acelera.plazoletamicroservice.adapters.http.dto.response.DishResponseDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IDishHandler {
    void saveDish(String userId,DishRequestDto dishRequestDto);
    void modifyDish( String userId,Long dishId, UpdateDishRequestDto updateDishRequestDto);
    void modifyDishState(String userId, Long dishId,boolean dishState);
    Page<DishResponseDto> getDishesByBudgetAndCategoryPreferences(Long budget, List<Long> categoryPreferencesId, int page, int size);

}
