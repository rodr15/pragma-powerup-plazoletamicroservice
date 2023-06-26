package com.ti.acelera.plazoletamicroservice.domain.api;

import com.ti.acelera.plazoletamicroservice.domain.model.Dish;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IDishServicePort {

    void saveDish(String userId,Dish dish);
    void modifyDish(String userId,Long dishId, Long price, String description);
    void modifyDishState( String proprietaryId, Long dishId, boolean dishState);
    Page<Dish> getDishesByBudgetAndCategoryPreferences(Long budget, List<Long> categoryPreferencesId,int page, int size);
}
