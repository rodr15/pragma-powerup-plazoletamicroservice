package com.ti.acelera.plazoletamicroservice.domain.usecase;

import com.ti.acelera.plazoletamicroservice.domain.exceptions.DishNotFoundException;
import com.ti.acelera.plazoletamicroservice.domain.exceptions.NotProprietaryGivenRestaurantException;
import com.ti.acelera.plazoletamicroservice.domain.exceptions.RestaurantNotExistsException;
import com.ti.acelera.plazoletamicroservice.domain.exceptions.RoleNotAllowedException;
import com.ti.acelera.plazoletamicroservice.domain.gateway.IUserClient;
import com.ti.acelera.plazoletamicroservice.domain.model.Dish;
import com.ti.acelera.plazoletamicroservice.domain.model.Restaurant;
import com.ti.acelera.plazoletamicroservice.domain.spi.IDishPersistencePort;
import com.ti.acelera.plazoletamicroservice.domain.spi.IRestaurantPersistencePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DishUseCaseTest {

    private IDishPersistencePort dishPersistencePort;
    private IRestaurantPersistencePort restaurantPersistencePort;
    private IUserClient userClient;
    private DishUseCase dishUseCase;


    @BeforeEach
    void setUp() {
        dishPersistencePort = mock(IDishPersistencePort.class);
        restaurantPersistencePort = mock(IRestaurantPersistencePort.class);
        userClient = mock(IUserClient.class);
        dishUseCase = new DishUseCase(dishPersistencePort, restaurantPersistencePort, userClient);
    }

    @Test
    void saveDish_ValidData_Success() {

        Restaurant restaurant = new Restaurant();
        restaurant.setId(1L);

        Dish dish = new Dish();
        dish.setRestaurant(restaurant);
        dish.setActive(false);

        String userId = "1231231231";
        restaurant.setIdProprietary(userId);

        when(restaurantPersistencePort.getRestaurant(dish.getRestaurant().getId())).thenReturn(Optional.of(restaurant));
        when(userClient.getRoleByDni("1231231231")).thenReturn("ROLE_OWNER");

        dishUseCase.saveDish(userId, dish);

        verify(dishPersistencePort, times(1)).saveDish(dish);
    }

    @Test
    void saveDish_RestaurantNotExists_ThrowsException() {
        Restaurant restaurant = new Restaurant();
        restaurant.setId(1L);

        Dish dish = new Dish();
        dish.setRestaurant(restaurant);

        String userId = "1231231231";
        restaurant.setIdProprietary(userId);

        when(restaurantPersistencePort.getRestaurant(dish.getRestaurant().getId())).thenReturn(Optional.empty());

        assertThrows(RestaurantNotExistsException.class, () -> dishUseCase.saveDish(userId, dish));

        verify(dishPersistencePort, never()).saveDish(Mockito.any());
    }

    @Test
    void saveDish_RoleNotAllowed_ThrowsException() {
        String userId = "1231231231";

        Restaurant restaurant = new Restaurant();
        restaurant.setId(1L);
        restaurant.setIdProprietary(userId);

        Dish dish = new Dish();
        dish.setRestaurant(restaurant);


        when(restaurantPersistencePort.getRestaurant(dish.getRestaurant().getId())).thenReturn(Optional.of(restaurant));
        when(userClient.getRoleByDni("1231231231")).thenReturn("ROLE_USER");

        assertThrows(RoleNotAllowedException.class, () -> dishUseCase.saveDish(userId, dish));

        verify(dishPersistencePort, never()).saveDish(Mockito.any());
    }

    @Test
    void saveDish_DifferentProprietary_ThrowsException() {
        Restaurant restaurant = new Restaurant();
        restaurant.setId(1L);
        restaurant.setIdProprietary("987654321");

        Dish dish = new Dish();
        dish.setRestaurant(restaurant);

        String userId = "1231231231";

        when(restaurantPersistencePort.getRestaurant(dish.getRestaurant().getId())).thenReturn(Optional.of(restaurant));
        when(userClient.getRoleByDni(userId)).thenReturn("ROLE_OWNER");

        assertThrows(NotProprietaryGivenRestaurantException.class, () -> dishUseCase.saveDish(userId, dish));

        verify(dishPersistencePort, never()).saveDish(Mockito.any());
    }

    @Test
    void testModifyDish_WhenDishExists_ShouldModifyDish() {
        // Arrange
        Long dishId = 1L;
        Long price = 10L;
        String description = "New description";
        String userId = "123456";

        Restaurant restaurant = new Restaurant();
        restaurant.setIdProprietary(userId);

        Dish dish = new Dish();
        dish.setPrice(20L);
        dish.setDescription("Old description");
        dish.setRestaurant(restaurant);

        Optional<Dish> dishOptional = Optional.of(dish);
        when(dishPersistencePort.getDish(dishId)).thenReturn(dishOptional);

        when(restaurantPersistencePort.getRestaurant(dish.getRestaurant().getId())).thenReturn(Optional.of(restaurant));
        when(userClient.getRoleByDni(userId)).thenReturn("ROLE_OWNER");
        // Act
        dishUseCase.modifyDish(userId, dishId, price, description);

        // Assert
        assertEquals(price, dish.getPrice());
        assertEquals(description, dish.getDescription());
        verify(dishPersistencePort, times(1)).saveDish(dish);
    }

    @Test
    void testModifyDish_WhenDishDoesNotExist_ShouldThrowDishNotFoundException() {
        // Arrange
        Long dishId = 1L;
        Long price = 10L;
        String description = "New description";
        String userId = "123456";

        Optional<Dish> dishOptional = Optional.empty();
        when(dishPersistencePort.getDish(dishId)).thenReturn(dishOptional);

        // Act & Assert
        assertThrows(DishNotFoundException.class, () ->
                dishUseCase.modifyDish(userId, dishId, price, description));
        verify(dishPersistencePort, never()).saveDish(any(Dish.class));
    }
}
