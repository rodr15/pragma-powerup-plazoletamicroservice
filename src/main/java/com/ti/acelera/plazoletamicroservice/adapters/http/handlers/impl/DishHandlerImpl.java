package com.ti.acelera.plazoletamicroservice.adapters.http.handlers.impl;

import com.ti.acelera.plazoletamicroservice.adapters.http.dto.request.DishRequestDto;
import com.ti.acelera.plazoletamicroservice.adapters.http.dto.request.UpdateDishRequestDto;
import com.ti.acelera.plazoletamicroservice.adapters.http.dto.response.DishResponseDto;
import com.ti.acelera.plazoletamicroservice.adapters.http.handlers.IDishHandler;
import com.ti.acelera.plazoletamicroservice.adapters.http.mapper.IDishRequestMapper;
import com.ti.acelera.plazoletamicroservice.adapters.http.mapper.IDishResponseMapper;
import com.ti.acelera.plazoletamicroservice.domain.api.IDishServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DishHandlerImpl implements IDishHandler {
    private final IDishServicePort dishServicePort;
    private final IDishRequestMapper dishRequestMapper;
    private final IDishResponseMapper dishResponseMapper;

    @Override
    public void saveDish(String userId, DishRequestDto dishRequestDto) {

        dishServicePort.saveDish(userId, dishRequestMapper.toDish(dishRequestDto));
    }

    @Override
    public void modifyDish(String userId, Long dishId, UpdateDishRequestDto updateDishRequestDto) {
        dishServicePort.modifyDish(userId, dishId, updateDishRequestDto.getPrice(), updateDishRequestDto.getDescription());
    }

    @Override
    public void modifyDishState(String userId, Long dishId, boolean dishState) {
        dishServicePort.modifyDishState(userId, dishId, dishState);
    }

    @Override
    public Page<DishResponseDto> getDishesByBudgetAndCategoryPreferences(Long lowBudget, Long upBudget, List<Long> categoryPreferencesId, int page, int size) {
        return dishServicePort
                .getDishesByBudgetAndCategoryPreferences(lowBudget,upBudget ,categoryPreferencesId, page, size)
                .map(dishResponseMapper::toResponseDto);
    }

}
