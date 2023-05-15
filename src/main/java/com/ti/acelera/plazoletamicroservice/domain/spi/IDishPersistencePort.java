package com.ti.acelera.plazoletamicroservice.domain.spi;

import com.ti.acelera.plazoletamicroservice.domain.model.Dish;

import java.util.Optional;

public interface IDishPersistencePort {
    void saveDish(Dish dish);

    Optional<Dish> getDish(Long id);
}
