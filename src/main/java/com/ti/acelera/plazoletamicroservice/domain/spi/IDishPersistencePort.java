package com.ti.acelera.plazoletamicroservice.domain.spi;

import com.ti.acelera.plazoletamicroservice.domain.model.Dish;

public interface IDishPersistencePort {
    void saveDish(Dish dish);
}
