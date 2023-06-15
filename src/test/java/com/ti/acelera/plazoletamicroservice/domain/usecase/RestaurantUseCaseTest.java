package com.ti.acelera.plazoletamicroservice.domain.usecase;

import com.ti.acelera.plazoletamicroservice.domain.exceptions.*;
import com.ti.acelera.plazoletamicroservice.domain.gateway.ITraceabilityClient;
import com.ti.acelera.plazoletamicroservice.domain.gateway.IUserClient;
import com.ti.acelera.plazoletamicroservice.domain.model.*;
import com.ti.acelera.plazoletamicroservice.domain.spi.IDishOrderPersistencePort;
import com.ti.acelera.plazoletamicroservice.domain.spi.IDishPersistencePort;
import com.ti.acelera.plazoletamicroservice.domain.spi.IOrderRestaurantPersistencePort;
import com.ti.acelera.plazoletamicroservice.domain.spi.IRestaurantPersistencePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RestaurantUseCaseTest {

    private RestaurantUseCase restaurantUseCase;

    @Mock
    private IRestaurantPersistencePort restaurantPersistencePort;
    @Mock
    private IDishPersistencePort dishPersistencePort;
    @Mock
    private IOrderRestaurantPersistencePort orderRestaurantPersistencePort;
    @Mock
    private IDishOrderPersistencePort dishOrderPersistencePort;
    @Mock
    private IUserClient userClient;
    @Mock
    private ITraceabilityClient traceabilityClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        restaurantUseCase = new RestaurantUseCase(restaurantPersistencePort, dishPersistencePort,orderRestaurantPersistencePort , dishOrderPersistencePort,userClient,traceabilityClient);
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
        verify(restaurantPersistencePort, times(0)).saveRestaurant(restaurant);
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

    @Test
    void testPageDishWithNullCategoryId() {
        // Mock restaurant
        Restaurant restaurant = new Restaurant();
        restaurant.setId(1L);
        Optional<Restaurant> optionalRestaurant = Optional.of(restaurant);
        when(restaurantPersistencePort.getRestaurant(anyLong())).thenReturn(optionalRestaurant);

        // Mock dish page
        List<Dish> dishList = new ArrayList<>();
        dishList.add(new Dish());
        dishList.add(new Dish());
        Page<Dish> dishPage = new PageImpl<>(dishList);
        when(dishPersistencePort.getActiveDishesByRestaurantId(anyLong(), anyInt(), anyInt())).thenReturn(dishPage);

        // Call the method
        int page = 0;
        int size = 10;
        Page<Dish> result = restaurantUseCase.pageDish(1L, null, page, size);

        // Assertions
        assertEquals(dishPage, result);
    }

    @Test
    void testPageDishWithNonNullCategoryId() {
        // Mock restaurant
        Restaurant restaurant = new Restaurant();
        restaurant.setId(1L);
        Optional<Restaurant> optionalRestaurant = Optional.of(restaurant);
        when(restaurantPersistencePort.getRestaurant(anyLong())).thenReturn(optionalRestaurant);

        // Mock dish page
        List<Dish> dishList = new ArrayList<>();
        dishList.add(new Dish());
        dishList.add(new Dish());
        Page<Dish> dishPage = new PageImpl<>(dishList);
        when(dishPersistencePort.getActiveDishesByRestaurantId(anyLong(), anyLong(), anyInt(), anyInt())).thenReturn(dishPage);

        // Call the method
        int page = 0;
        int size = 10;
        Page<Dish> result = restaurantUseCase.pageDish(1L, 2L, page, size);

        // Assertions
        assertEquals(dishPage, result);
    }

    @Test
    void testPageDishWithInvalidPageAndSize() {
        // Call the method with invalid page and size
        int invalidPage = -1;
        int invalidSize = 0;

        assertThrows(BadPagedException.class, () -> restaurantUseCase.pageDish(1L, null, invalidPage, invalidSize));
    }

    @Test
    void testPageDishWithNonExistingRestaurant() {
        // Mock non-existing restaurant
        Optional<Restaurant> optionalRestaurant = Optional.empty();
        when(restaurantPersistencePort.getRestaurant(anyLong())).thenReturn(optionalRestaurant);

        // Call the method
        int page = 0;
        int size = 10;

        assertThrows(RestaurantNotExistsException.class, () ->
                restaurantUseCase.pageDish(1L, null, page, size));

    }

    @Test
    void makeOrder_WithNoUnfinishedOrdersAndValidRestaurant_ShouldCreateOrder() {

        // Arrange
        Restaurant validRestaurant = new Restaurant(1L, "Mock", "ABC", "1", "321312312", "", "", new HashSet<>());

        Category category = new Category();
        Dish dish = new Dish(1L, "Dish 1", category, "Moch Dish 1", 123L, validRestaurant, "", true);

        DishOrder dishOrder = new DishOrder(null, dish, 2);

        List<DishOrder> dishOrders = List.of(dishOrder);
        List<Dish> validDishList = List.of(dish);
        OrderRestaurant orderRestaurant = new OrderRestaurant(1L, 1L, LocalDate.now(), null, null, validRestaurant, dishOrders);


        when(orderRestaurantPersistencePort.hasUnfinishedOrders(orderRestaurant.getIdClient())).thenReturn(false);
        when(restaurantPersistencePort.restaurantExists(orderRestaurant.getRestaurant().getId())).thenReturn(true);
        when(dishPersistencePort.findAllDishesByIdAndByRestaurantId(anyLong(), anyList())).thenReturn(validDishList);
        when(orderRestaurantPersistencePort.createNewOrder(orderRestaurant)).thenReturn(orderRestaurant);

        // Act
        Long orderId = restaurantUseCase.makeOrder(orderRestaurant);

        // Assert

        assertEquals(1L, orderId);
        verify(orderRestaurantPersistencePort, times(1)).hasUnfinishedOrders(orderRestaurant.getIdClient());
        verify(restaurantPersistencePort, times(1)).restaurantExists(orderRestaurant.getRestaurant().getId());
        verify(dishPersistencePort, times(1)).findAllDishesByIdAndByRestaurantId(anyLong(), anyList());
        verify(orderRestaurantPersistencePort, times(1)).createNewOrder(orderRestaurant);
    }


    @Test
    void makeOrder_WithUnfinishedOrdersAndValidRestaurant_ShouldNotCreateOrder() {

        // Arrange
        Restaurant validRestaurant = new Restaurant(1L, "Mock", "ABC", "1", "321312312", "", "", new HashSet<>());

        Category category = new Category();
        Dish dish = new Dish(1L, "Dish 1", category, "Moch Dish 1", 123L, validRestaurant, "", true);

        DishOrder dishOrder = new DishOrder(null, dish, 2);

        List<DishOrder> dishOrders = List.of(dishOrder);
        OrderRestaurant orderRestaurant = new OrderRestaurant(1L, 1L, LocalDate.now(), null, null, validRestaurant, dishOrders);


        when(orderRestaurantPersistencePort.hasUnfinishedOrders(orderRestaurant.getIdClient())).thenReturn(true);

        // Act
        assertThrows(ThisClientHasUnfinishedOrdersException.class, () -> restaurantUseCase.makeOrder(orderRestaurant));

        // Assert
        verify(orderRestaurantPersistencePort, times(1)).hasUnfinishedOrders(orderRestaurant.getIdClient());
        verify(restaurantPersistencePort, times(0)).restaurantExists(anyLong());
        verify(dishPersistencePort, times(0)).findAllDishesByIdAndByRestaurantId(anyLong(), anyList());
        verify(orderRestaurantPersistencePort, times(0)).createNewOrder(new OrderRestaurant());
    }

    @Test
    void makeOrder_WithNotUnfinishedOrdersAndNotValidRestaurant_ShouldNotCreateOrder() {

        // Arrange
        Restaurant validRestaurant = new Restaurant(1L, "Mock", "ABC", "1", "321312312", "", "", new HashSet<>());

        Category category = new Category();
        Dish dish = new Dish(1L, "Dish 1", category, "Moch Dish 1", 123L, validRestaurant, "", true);

        DishOrder dishOrder = new DishOrder(null, dish, 2);

        List<DishOrder> dishOrders = List.of(dishOrder);
        OrderRestaurant orderRestaurant = new OrderRestaurant(1L, 1L, LocalDate.now(), null, null, validRestaurant, dishOrders);


        when(orderRestaurantPersistencePort.hasUnfinishedOrders(orderRestaurant.getIdClient())).thenReturn(false);
        when(restaurantPersistencePort.restaurantExists(orderRestaurant.getRestaurant().getId())).thenReturn(false);
        // Act
        assertThrows(RestaurantNotExistsException.class, () -> restaurantUseCase.makeOrder(orderRestaurant));

        // Assert
        verify(orderRestaurantPersistencePort, times(1)).hasUnfinishedOrders(orderRestaurant.getIdClient());
        verify(restaurantPersistencePort, times(1)).restaurantExists(anyLong());
        verify(dishPersistencePort, times(0)).findAllDishesByIdAndByRestaurantId(anyLong(), anyList());
        verify(orderRestaurantPersistencePort, times(0)).createNewOrder(new OrderRestaurant());
    }


    @Test
    void makeOrder_WithNotUnfinishedOrdersAndValidRestaurantAndInvalidDishOrder_ShouldNotCreateOrder() {

        // Arrange
        Restaurant validRestaurant = new Restaurant(1L, "Mock", "ABC", "1", "321312312", "", "", new HashSet<>());

        Category category = new Category();
        Dish dish = new Dish(1L, "Dish 1", category, "Moch Dish 1", 123L, validRestaurant, "", true);

        DishOrder dishOrder = new DishOrder(null, dish, 2);

        List<DishOrder> dishOrders = List.of(dishOrder, dishOrder);
        OrderRestaurant orderRestaurant = new OrderRestaurant(1L, 1L, LocalDate.now(), null, null, validRestaurant, dishOrders);


        when(orderRestaurantPersistencePort.hasUnfinishedOrders(orderRestaurant.getIdClient())).thenReturn(false);
        when(restaurantPersistencePort.restaurantExists(orderRestaurant.getRestaurant().getId())).thenReturn(true);
        // Act
        assertThrows(MalformedOrderException.class, () -> restaurantUseCase.makeOrder(orderRestaurant));

        // Assert
        verify(orderRestaurantPersistencePort, times(1)).hasUnfinishedOrders(orderRestaurant.getIdClient());
        verify(restaurantPersistencePort, times(1)).restaurantExists(anyLong());
        verify(dishPersistencePort, times(0)).findAllDishesByIdAndByRestaurantId(anyLong(), anyList());
        verify(orderRestaurantPersistencePort, times(0)).createNewOrder(new OrderRestaurant());
    }


    @Test
    void makeOrder_WithNotUnfinishedOrdersAndValidRestaurantAndValidDishOrderAndInvalidDishesId_ShouldNotCreateOrder() {

        // Arrange
        Restaurant validRestaurant = new Restaurant(1L, "Mock", "ABC", "1", "321312312", "", "", new HashSet<>());

        Category category = new Category();
        Dish dish = new Dish(1L, "Dish 1", category, "Moch Dish 1", 123L, validRestaurant, "", true);

        DishOrder dishOrder = new DishOrder(null, dish, 2);

        List<DishOrder> dishOrders = List.of(dishOrder);
        List<Dish> validDishList = List.of();
        OrderRestaurant orderRestaurant = new OrderRestaurant(1L, 1L, LocalDate.now(), null, null, validRestaurant, dishOrders);


        when(orderRestaurantPersistencePort.hasUnfinishedOrders(orderRestaurant.getIdClient())).thenReturn(false);
        when(restaurantPersistencePort.restaurantExists(orderRestaurant.getRestaurant().getId())).thenReturn(true);
        when(dishPersistencePort.findAllDishesByIdAndByRestaurantId(anyLong(), anyList())).thenReturn(validDishList);
        // Act
        assertThrows(DishNotFoundException.class, () -> restaurantUseCase.makeOrder(orderRestaurant));

        // Assert
        verify(orderRestaurantPersistencePort, times(1)).hasUnfinishedOrders(orderRestaurant.getIdClient());
        verify(restaurantPersistencePort, times(1)).restaurantExists(anyLong());
        verify(dishPersistencePort, times(1)).findAllDishesByIdAndByRestaurantId(anyLong(), anyList());
        verify(orderRestaurantPersistencePort, times(0)).createNewOrder(new OrderRestaurant());
    }

    @Test
    void getOrdersList_ValidEmployeeId_ReturnsOrdersList() {
        // Arrange
        Long employeeId = 123L;
        Long restaurantId = 1L;
        String state = "FINISHED";
        int page = 0;
        int size = 10;

        Restaurant restaurant = new Restaurant(1L, "Mock", "ABC", "1", "321312312", "", "", new HashSet<>());
        OrderRestaurant orderRestaurant = new OrderRestaurant(1L, 111L, LocalDate.now(), state, 5L, restaurant, List.of());
        List<OrderRestaurant> orders = Collections.singletonList(orderRestaurant);
        Page<OrderRestaurant> ordersPage = new PageImpl<>(orders, PageRequest.of(page, size), orders.size());


        // Mock restaurantPersistencePort
        when(restaurantPersistencePort.getRestaurantIdByEmployeeId(employeeId)).thenReturn(Optional.of(restaurantId));

        // Mock orderRestaurantPersistencePort
        when(orderRestaurantPersistencePort.getOrdersList(restaurantId, state, page, size)).thenReturn(ordersPage);

        // Mock dishOrderPersistencePort
        List<DishOrder> dishOrders = Collections.singletonList(new DishOrder());
        when(dishOrderPersistencePort.getDishOrderByOrderRestaurantId(restaurantId)).thenReturn(dishOrders);

        // Act
        Page<OrderRestaurant> result = restaurantUseCase.getOrdersList(employeeId, state, page, size);

        // Assert
        assertEquals(ordersPage, result);
        assertEquals(dishOrders, result.getContent().get(0).getDishes());
        verify(restaurantPersistencePort, times(1)).getRestaurantIdByEmployeeId(employeeId);
        verify(orderRestaurantPersistencePort, times(1)).getOrdersList(restaurantId, state, page, size);
        verify(dishOrderPersistencePort, times(1)).getDishOrderByOrderRestaurantId(restaurantId);
    }

    @Test
    void getOrdersList_InvalidEmployeeId_ThrowsException() {
        // Arrange
        Long employeeId = 123L;
        String state = "FINISHED";
        int page = 0;
        int size = 10;

        // Mock restaurantPersistencePort
        when(restaurantPersistencePort.getRestaurantIdByEmployeeId(employeeId)).thenReturn(Optional.empty());

        // Act and assert
        assertThrows(RestaurantNotExistsException.class, () ->
                restaurantUseCase.getOrdersList(employeeId, state, page, size));

        verify(restaurantPersistencePort, times(1)).getRestaurantIdByEmployeeId(employeeId);
        verifyNoInteractions(orderRestaurantPersistencePort);
        verifyNoInteractions(dishOrderPersistencePort);
    }

    @Test
    void getOrdersList_EmptyDishOrders_ShouldThrowMalformedOrderException() {
        // Arrange
        Long employeeId = 123L;
        String state = "OPEN";
        int page = 0;
        int size = 10;
        Long restaurantId = 456L;

        Restaurant restaurant = new Restaurant(1L, "Mock", "ABC", "1", "321312312", "", "", new HashSet<>());
        OrderRestaurant orderRestaurant = new OrderRestaurant(1L, 111L, LocalDate.now(), state, 5L, restaurant, List.of());

        // Mocking the restaurantPersistencePort
        when(restaurantPersistencePort.getRestaurantIdByEmployeeId(employeeId)).thenReturn(Optional.of(restaurantId));

        // Mocking the orderRestaurantPersistencePort
        List<OrderRestaurant> mockOrders = List.of(orderRestaurant, orderRestaurant);// Create a list of mock orders
        Page<OrderRestaurant> mockPage = new PageImpl<>(mockOrders); // Create a mock Page object
        when(orderRestaurantPersistencePort.getOrdersList(restaurantId, state, page, size)).thenReturn(mockPage);

        // Mocking the dishOrderPersistencePort with empty dish orders
        List<DishOrder> mockDishOrders = new ArrayList<>(); // Empty list of dish orders
        when(dishOrderPersistencePort.getDishOrderByOrderRestaurantId(anyLong())).thenReturn(mockDishOrders);

        // Act and Assert
        assertThrows(MalformedOrderException.class, () ->
                restaurantUseCase.getOrdersList(employeeId, state, page, size));

        verify(restaurantPersistencePort, times(1)).getRestaurantIdByEmployeeId(employeeId);
        verify(orderRestaurantPersistencePort, times(1)).getOrdersList(restaurantId, state, page, size);
        verify(dishOrderPersistencePort, times(1)).getDishOrderByOrderRestaurantId(anyLong());
    }
}
