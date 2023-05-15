package com.ti.acelera.plazoletamicroservice.domain.usecase;

import com.ti.acelera.plazoletamicroservice.domain.api.IDishServicePort;
import com.ti.acelera.plazoletamicroservice.domain.exceptions.RestaurantNotExistsException;
import com.ti.acelera.plazoletamicroservice.domain.exceptions.RoleNotAllowedException;
import com.ti.acelera.plazoletamicroservice.domain.gateway.IUserClient;
import com.ti.acelera.plazoletamicroservice.domain.model.Dish;
import com.ti.acelera.plazoletamicroservice.domain.spi.IDishPersistencePort;
import com.ti.acelera.plazoletamicroservice.domain.spi.IRestaurantPersistencePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DishUseCaseTest {

    private DishUseCase dishUseCase;

    @Mock
    private IDishPersistencePort dishPersistencePort;

    @Mock
    private IRestaurantPersistencePort restaurantPersistencePort;

    @Mock
    private IUserClient userClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        dishUseCase = new DishUseCase(dishPersistencePort, restaurantPersistencePort, userClient);
    }

    @Test
    void saveDish_ValidDishData_DishSaved() {
        Dish dish = new Dish();
        dish.setIdRestaurant(1L);
        dish.setName("Pizza");
        dish.setDescription("Delicious pizza");
        dish.setPrice(10L);

        when(restaurantPersistencePort.restaurantExists(dish.getIdRestaurant())).thenReturn(true);
        when(userClient.getRoleByDni(anyString())).thenReturn("ROLE_OWNER");

        assertDoesNotThrow(() -> dishUseCase.saveDish(dish));
        assertTrue(dish.isActive());
        verify(dishPersistencePort, times(1)).saveDish(dish);
    }

    @Test
    void saveDish_RestaurantNotExists_ExceptionThrown() {
        Dish dish = new Dish();
        dish.setIdRestaurant(1L);
        dish.setName("Pizza");
        dish.setDescription("Delicious pizza");
        dish.setPrice(10L);

        when(restaurantPersistencePort.restaurantExists(dish.getIdRestaurant())).thenReturn(false);

        assertThrows(RestaurantNotExistsException.class, () -> dishUseCase.saveDish(dish));
        assertFalse(dish.isActive());
        verify(dishPersistencePort, never()).saveDish(dish);
    }

    @Test
    void saveDish_UserNotOwner_ExceptionThrown() {
        Dish dish = new Dish();
        dish.setIdRestaurant(1L);
        dish.setName("Pizza");
        dish.setDescription("Delicious pizza");
        dish.setPrice(10L);

        when(restaurantPersistencePort.restaurantExists(dish.getIdRestaurant())).thenReturn(true);
        when(userClient.getRoleByDni(anyString())).thenReturn("ROLE_USER");

        assertThrows(RoleNotAllowedException.class, () -> dishUseCase.saveDish(dish));
        assertFalse(dish.isActive());
        verify(dishPersistencePort, never()).saveDish(dish);
    }
}
