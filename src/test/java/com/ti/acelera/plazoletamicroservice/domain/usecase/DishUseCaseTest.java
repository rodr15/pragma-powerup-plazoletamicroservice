package com.ti.acelera.plazoletamicroservice.domain.usecase;

import com.ti.acelera.plazoletamicroservice.domain.exceptions.RestaurantNotExistsException;
import com.ti.acelera.plazoletamicroservice.domain.model.Dish;
import com.ti.acelera.plazoletamicroservice.domain.spi.IDishPersistencePort;
import com.ti.acelera.plazoletamicroservice.domain.spi.IRestaurantPersistencePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DishUseCaseTest {
    @Mock
    private IDishPersistencePort dishPersistencePort;

    @Mock
    private IRestaurantPersistencePort restaurantPersistencePort;

    private DishUseCase dishUseCase;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        dishUseCase = new DishUseCase(dishPersistencePort, restaurantPersistencePort, userClient);
    }

    @Test
    public void testSaveDish_WithExistingRestaurant_ShouldSaveDish() {
        Dish dish = new Dish(
                "Restaurant Name", "idCategory", "Some Description", 12345L, 1L, "", true
        );
        dish.setIdRestaurant(1L);

        when(restaurantPersistencePort.restaurantExists(1L)).thenReturn(true);


        assertDoesNotThrow(() -> dishUseCase.saveDish(dish));
        assertTrue(dish.isActive());
        verify(dishPersistencePort, times(1)).saveDish(dish);
    }

    @Test
    public void testSaveDish_WithNonExistingRestaurant_ShouldThrowException() {
        Dish dish = new Dish(
                "Restaurant Name", "idCategory", "Some Description", 12345L, 1L, "", true
        );
        dish.setIdRestaurant(2L);

        when(restaurantPersistencePort.restaurantExists(2L)).thenReturn(false);

        assertThrows(RestaurantNotExistsException.class, () -> dishUseCase.saveDish(dish));

        verify(dishPersistencePort, never()).saveDish(dish);
    }
}