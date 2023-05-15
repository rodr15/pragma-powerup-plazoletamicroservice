package com.ti.acelera.plazoletamicroservice.domain.usecase;

import com.ti.acelera.plazoletamicroservice.domain.model.Dish;
import com.ti.acelera.plazoletamicroservice.domain.spi.IDishPersistencePort;

public class DishUseCase implements IDishPersistencePort {

    private final IDishPersistencePort persistencePort;

    public DishUseCase(IDishPersistencePort persistencePort) {
        this.persistencePort = persistencePort;
    }


    @Override
    public void saveDish(Dish dish) {
        persistencePort.saveDish( dish );
    }
}
