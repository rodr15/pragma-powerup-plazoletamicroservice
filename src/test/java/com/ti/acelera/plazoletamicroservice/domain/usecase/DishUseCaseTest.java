package com.ti.acelera.plazoletamicroservice.domain.usecase;

import com.ti.acelera.plazoletamicroservice.domain.api.IDishServicePort;
import com.ti.acelera.plazoletamicroservice.domain.exceptions.*;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DishUseCaseTest {

    private IDishPersistencePort dishPersistencePort;
    private IRestaurantPersistencePort restaurantPersistencePort;
    private IUserClient userClient;
    private IDishServicePort dishService;


    @BeforeEach
    void setUp() {
        dishPersistencePort = mock(IDishPersistencePort.class);
        restaurantPersistencePort = mock(IRestaurantPersistencePort.class);
        userClient = mock(IUserClient.class);
        dishService = new DishUseCase(dishPersistencePort, restaurantPersistencePort, userClient);
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

        dishService.saveDish(userId, dish);

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

        assertThrows(RestaurantNotExistsException.class, () -> dishService.saveDish(userId, dish));

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

        assertThrows(RoleNotAllowedException.class, () -> dishService.saveDish(userId, dish));

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

        assertThrows(NotProprietaryGivenRestaurantException.class, () -> dishService.saveDish(userId, dish));

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

        when(userClient.getRoleByDni(userId)).thenReturn("ROLE_OWNER");
        // Act
        dishService.modifyDish(userId, dishId, price, description);

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
                dishService.modifyDish(userId, dishId, price, description));
        verify(dishPersistencePort, never()).saveDish(any(Dish.class));
    }

    @Test
    void testModifyDishState_ValidDishIdAndProprietaryId_DishStateModified() {
        // Arrange
        String proprietaryId = "123456";

        Restaurant restaurant = new Restaurant();
        restaurant.setIdProprietary(proprietaryId);

        Long dishId = 1L;
        boolean dishState = false;
        Dish dish = new Dish();
        dish.setRestaurant(restaurant);

        Optional<Dish> optionalDish = Optional.of(dish);

        when(dishPersistencePort.getDish(dishId)).thenReturn(optionalDish);
        when(userClient.getRoleByDni(proprietaryId)).thenReturn("ROLE_OWNER");

        // Act
        dishService.modifyDishState(proprietaryId, dishId, dishState);

        // Assert
        assertFalse(dish.isActive());
        verify(dishPersistencePort, times(1)).getDish(dishId);
        verify(dishPersistencePort, times(1)).saveDish(dish);
    }

    @Test
    void testModifyDishState_InvalidDishId_DishNotFoundExceptionThrown() {
        // Arrange
        Long dishId = 1L;
        String proprietaryId = "123456";
        boolean dishState = true;
        Optional<Dish> optionalDish = Optional.empty();

        when(dishPersistencePort.getDish(dishId)).thenReturn(optionalDish);

        // Act & Assert
        assertThrows(DishNotFoundException.class,
                () -> dishService.modifyDishState(proprietaryId, dishId, dishState));
        verify(dishPersistencePort, times(1)).getDish(dishId);
        verify(dishPersistencePort, never()).saveDish(any(Dish.class));
    }

    @Test
    void testModifyDishState_NotProprietaryGivenRestaurant_NotProprietaryGivenRestaurantExceptionThrown() {
        // Arrange
        String proprietaryId = "123456";
        String otherProprietaryId = "654321";

        Long dishId = 1L;
        boolean dishState = true;
        Dish dish = new Dish();

        Restaurant restaurant = new Restaurant();
        restaurant.setIdProprietary(otherProprietaryId);

        dish.setRestaurant(restaurant);

        Optional<Dish> optionalDish = Optional.of(dish);

        when(dishPersistencePort.getDish(dishId)).thenReturn(optionalDish);
        when(userClient.getRoleByDni(proprietaryId)).thenReturn("ROLE_OWNER");

        // Act & Assert
        assertThrows(NotProprietaryGivenRestaurantException.class,
                () -> dishService.modifyDishState(proprietaryId, dishId, dishState));
        verify(dishPersistencePort, times(1)).getDish(dishId);
        verify(dishPersistencePort, never()).saveDish(any(Dish.class));
    }
    @Test
    void getDishesByBudgetAndCategoryPreferences_ValidValues_ReturnsPageOfDishes() {
        // Arrange
        Long lowBudget = 10L;
        Long upBudget = 20L;
        Long categoryPreferencesId = 2L;
        int page = 0;
        int size = 10;

        Page<Dish> expectedPage = createMockPageOfDishes();

        when(dishPersistencePort.getDishesByBudgetAndCategoryPreferences(anyLong(), anyLong(), anyLong(), any(Pageable.class)))
                .thenReturn(expectedPage);

        // Act
        Page<Dish> result = dishService.getDishesByBudgetAndCategoryPreferences(lowBudget, upBudget, categoryPreferencesId, page, size);

        // Assert
        assertNotNull(result);
        assertEquals(expectedPage, result);
    }

    @Test
    void getDishesByBudgetAndCategoryPreferences_EmptyCategoryPreferences_ReturnsPageOfDishes() {
        // Arrange
        Long lowBudget = 10L;
        Long upBudget = 20L;
        Long categoryPreferencesId = null;
        int page = 0;
        int size = 10;

        Page<Dish> expectedPage = createMockPageOfDishes();

        when(dishPersistencePort.getDishesByBudgetAndCategoryPreferences(anyLong(), anyLong(), isNull(), any(Pageable.class)))
                .thenReturn(expectedPage);

        // Act
        Page<Dish> result = dishService.getDishesByBudgetAndCategoryPreferences(lowBudget, upBudget, categoryPreferencesId, page, size);

        // Assert
        assertNotNull(result);
    }

    @Test
    void getDishesByBudgetAndCategoryPreferences_SameLowAndUpBudget_ReturnsPageOfDishes() {
        // Arrange
        Long lowBudget = 20L;
        Long upBudget = 20L;
        Long categoryPreferencesId = 2L;
        int page = 0;
        int size = 10;

        Page<Dish> expectedPage = createMockPageOfDishes();

        when(dishPersistencePort.getDishesByBudgetAndCategoryPreferences(anyLong(), anyLong(), anyLong(), any(Pageable.class)))
                .thenReturn(expectedPage);

        // Act
        Page<Dish> result = dishService.getDishesByBudgetAndCategoryPreferences(lowBudget, upBudget, categoryPreferencesId, page, size);

        // Assert
        assertNotNull(result);
        assertEquals(expectedPage, result);
    }

    @Test
    void getDishesByBudgetAndCategoryPreferences_InvalidPageAndSize_ThrowsBadPagedException() {
        // Arrange
        Long lowBudget = 10L;
        Long upBudget = 20L;
        Long categoryPreferencesId = 2L;
        int page = -1;
        int size = 0;

        // Act & Assert
        assertThrows(BadPagedException.class,
                () -> dishService.getDishesByBudgetAndCategoryPreferences(lowBudget, upBudget, categoryPreferencesId, page, size));
    }

    private Page<Dish> createMockPageOfDishes() {


        List<Dish> dishes = List.of(
                new Dish(),
                new Dish()
        );

        return new PageImpl<>(dishes);
    }

}
