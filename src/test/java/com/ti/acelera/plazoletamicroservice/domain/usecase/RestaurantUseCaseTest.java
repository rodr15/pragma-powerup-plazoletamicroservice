package com.ti.acelera.plazoletamicroservice.domain.usecase;

import com.ti.acelera.plazoletamicroservice.domain.exceptions.BadPagedException;
import com.ti.acelera.plazoletamicroservice.domain.exceptions.RestaurantNotExistsException;
import com.ti.acelera.plazoletamicroservice.domain.exceptions.RoleNotAllowedException;
import com.ti.acelera.plazoletamicroservice.domain.gateway.IUserClient;
import com.ti.acelera.plazoletamicroservice.domain.model.Restaurant;
import com.ti.acelera.plazoletamicroservice.domain.spi.IRestaurantPersistencePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RestaurantUseCaseTest {

    private RestaurantUseCase restaurantUseCase;

    @Mock
    private IRestaurantPersistencePort restaurantPersistencePort;

    @Mock
    private IUserClient userClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        restaurantUseCase = new RestaurantUseCase(restaurantPersistencePort, userClient);
    }

    @Test
    void saveRestaurant_ValidRestaurant_RestaurantSaved() {
        // Arrange
        Restaurant restaurant = new Restaurant(1L, "Restaurant 1", "CC SANTA", "123123123", "123456789", "Some Address", "12312332", new HashSet<>());
        when(userClient.getRoleByDni(restaurant.getIdProprietary())).thenReturn("ROLE_OWNER");

        // Act
        assertDoesNotThrow(() -> restaurantUseCase.saveRestaurant(restaurant));

        // Assert
        verify(userClient).getRoleByDni(restaurant.getIdProprietary());
        verify(restaurantPersistencePort).saveRestaurant(restaurant);
    }

    @Test
    void saveRestaurant_NonOwnerRole_ThrowsRoleNotAllowedException() {
        // Arrange
        Restaurant restaurant = new Restaurant(1L, "Restaurant 1", "CC SANTA", "123", "123456789", "Some Address", "12312332", new HashSet<>());
        when(userClient.getRoleByDni(restaurant.getIdProprietary())).thenReturn("ROLE_USER");

        // Act & Assert
        assertThrows(RoleNotAllowedException.class, () -> restaurantUseCase.saveRestaurant(restaurant));

        // Assert
        verify(userClient).getRoleByDni(restaurant.getIdProprietary());
        verify(restaurantPersistencePort,times(0)).saveRestaurant(restaurant);
    }

    @Test
    void testVerifyRestaurantOwner_ExistingRestaurantAndMatchingOwner_ReturnsTrue() {
        // Arrange
        String userId = "123456789";
        Long restaurantId = 1L;
        Restaurant restaurant = new Restaurant();
        restaurant.setIdProprietary(userId);

        when(restaurantPersistencePort.getRestaurant(restaurantId)).thenReturn(Optional.of(restaurant));

        // Act
        boolean result = restaurantUseCase.verifyRestaurantOwner(userId, restaurantId);

        // Assert
        assertTrue(result);
        verify(restaurantPersistencePort, times(1)).getRestaurant(restaurantId);
    }

    @Test
    void testVerifyRestaurantOwner_ExistingRestaurantAndNonMatchingOwner_ReturnsFalse() {
        // Arrange
        String userId = "123456789";
        Long restaurantId = 1L;
        Restaurant restaurant = new Restaurant();
        restaurant.setIdProprietary("987654321");

        when(restaurantPersistencePort.getRestaurant(restaurantId)).thenReturn(Optional.of(restaurant));

        // Act
        boolean result = restaurantUseCase.verifyRestaurantOwner(userId, restaurantId);

        // Assert
        assertFalse(result);
        verify(restaurantPersistencePort, times(1)).getRestaurant(restaurantId);
    }

    @Test
    void testVerifyRestaurantOwner_NonExistingRestaurant_ThrowsException() {
        // Arrange
        String userId = "123456789";
        Long restaurantId = 1L;

        when(restaurantPersistencePort.getRestaurant(restaurantId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RestaurantNotExistsException.class,
                () -> restaurantUseCase.verifyRestaurantOwner(userId, restaurantId));

        verify(restaurantPersistencePort, times(1)).getRestaurant(restaurantId);
    }

    @Test
    void testAssignEmployee_EmptyEmployeesSet_CreatesNewSetAndAddsEmployee() {
        // Arrange
        String userId = "123456789";
        Long restaurantId = 1L;
        Restaurant restaurant = new Restaurant();

        when(restaurantPersistencePort.getRestaurant(restaurantId)).thenReturn(Optional.of(restaurant));


        // Act
        restaurantUseCase.assignEmployee(userId, restaurantId);

        // Assert
        Set<String> employees = restaurant.getEmployees();
        assertNotNull(employees);
        assertEquals(1, (employees).size());
        assertTrue(employees.contains(userId));

        verify(restaurantPersistencePort, times(1)).getRestaurant(restaurantId);
        verify(restaurantPersistencePort, times(1)).saveRestaurant(restaurant);
    }

    @Test
    void testAssignEmployee_NonEmptyEmployeesSet_AddsEmployee() {
        // Arrange
        String userId = "123456789";
        Long restaurantId = 1L;
        Restaurant restaurant = new Restaurant();
        Set<String> existingEmployees = new HashSet<>();
        existingEmployees.add("987654321");
        restaurant.setEmployees(existingEmployees);

        when(restaurantPersistencePort.getRestaurant(restaurantId)).thenReturn(Optional.of(restaurant));


        // Act
        restaurantUseCase.assignEmployee(userId, restaurantId);

        // Assert
        Set<String> employees = restaurant.getEmployees();
        assertNotNull(employees);
        assertEquals(2, employees.size());
        assertTrue(employees.contains(userId));
        assertTrue(employees.contains("987654321"));

        verify(restaurantPersistencePort, times(1)).getRestaurant(restaurantId);
        verify(restaurantPersistencePort, times(1)).saveRestaurant(restaurant);
    }

    @Test
    void testAssignEmployee_NonExistingRestaurant_ThrowsException() {
        // Arrange
        String userId = "123456789";
        Long restaurantId = 1L;

        when(restaurantPersistencePort.getRestaurant(restaurantId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RestaurantNotExistsException.class,
                () -> restaurantUseCase.assignEmployee(userId, restaurantId));

        verify(restaurantPersistencePort, times(1)).getRestaurant(restaurantId);
        verify(restaurantPersistencePort, never()).saveRestaurant(any(Restaurant.class));
    }


    @Test
    void pageRestaurants_withValidPageAndSize_shouldReturnPageOfRestaurants() {
        // Arrange
        int page = 0;
        int size = 10;

        Page<Restaurant> expectedPage = mock(Page.class);
        when(restaurantPersistencePort.getAllRestaurants(page, size)).thenReturn(expectedPage);

        // Act
        Page<Restaurant> resultPage = restaurantUseCase.pageRestaurants(page, size);

        // Assert
        assertEquals(expectedPage, resultPage);
        verify(restaurantPersistencePort, times(1)).getAllRestaurants(page, size);
    }

    @Test
    void pageRestaurants_withInvalidPage_shouldThrowBadPagedException() {
        // Arrange
        int page = -1;
        int size = 10;

        // Act & Assert
        assertThrows(BadPagedException.class, () -> restaurantUseCase.pageRestaurants(page, size));
        verify(restaurantPersistencePort, never()).getAllRestaurants(anyInt(), anyInt());
    }

    @Test
    void pageRestaurants_withInvalidSize_shouldThrowBadPagedException() {
        // Arrange
        int page = 0;
        int size = 0;

        // Act & Assert
        assertThrows(BadPagedException.class, () -> restaurantUseCase.pageRestaurants(page, size));
        verify(restaurantPersistencePort, never()).getAllRestaurants(anyInt(), anyInt());
    }
}
