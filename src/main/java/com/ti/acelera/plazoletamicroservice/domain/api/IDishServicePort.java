package com.ti.acelera.plazoletamicroservice.domain.api;

import com.ti.acelera.plazoletamicroservice.domain.model.Dish;

public interface IDishServicePort {

    void saveDish(String userId,Dish dish);
    void modifyDish(String userId,Long dishId, Long price, String description);

}
