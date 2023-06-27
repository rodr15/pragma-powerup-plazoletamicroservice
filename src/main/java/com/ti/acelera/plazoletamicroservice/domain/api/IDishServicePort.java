package com.ti.acelera.plazoletamicroservice.domain.api;

import com.ti.acelera.plazoletamicroservice.domain.model.Dish;
import org.springframework.data.domain.Page;

public interface IDishServicePort {

    void saveDish(String userId,Dish dish);
    void modifyDish(String userId,Long dishId, Long price, String description);
    void modifyDishState( String proprietaryId, Long dishId, boolean dishState);
    Page<Dish> getDishesByBudgetAndCategoryPreferences(Long lowBudget,Long upBudget, Long categoryPreferencesId, int page, int size) ;
}
