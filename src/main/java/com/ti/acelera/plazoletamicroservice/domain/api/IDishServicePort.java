package com.ti.acelera.plazoletamicroservice.domain.api;

import com.ti.acelera.plazoletamicroservice.domain.model.Dish;

public interface IDishServicePort {

    void saveDish(Dish dish);
    void modifyDish(Long dishId, Long price, String description);

}
