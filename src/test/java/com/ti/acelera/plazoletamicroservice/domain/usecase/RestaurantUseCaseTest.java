package com.ti.acelera.plazoletamicroservice.domain.usecase;

import com.ti.acelera.plazoletamicroservice.domain.api.IRestaurantServicePort;
import com.ti.acelera.plazoletamicroservice.domain.exceptions.*;
import com.ti.acelera.plazoletamicroservice.domain.gateway.ISmsClient;
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

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RestaurantUseCaseTest {

    private IRestaurantServicePort restaurantUseCase;

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
    private ISmsClient smsClient;
    @Mock
    private ITraceabilityClient traceabilityClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        restaurantUseCase = new RestaurantUseCase(restaurantPersistencePort, dishPersistencePort, orderRestaurantPersistencePort, dishOrderPersistencePort, userClient,traceabilityClient,smsClient);
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
        restaurantUseCase.assignEmployeeToRestaurant(userId, restaurantId);

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
        restaurantUseCase.assignEmployeeToRestaurant(userId, restaurantId);

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
                () -> restaurantUseCase.assignEmployeeToRestaurant(userId, restaurantId));

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
        OrderRestaurant orderRestaurant = new OrderRestaurant(1L, 1L, LocalDateTime.now(), null, null, validRestaurant, dishOrders,null);


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
        OrderRestaurant orderRestaurant = new OrderRestaurant(1L, 1L, LocalDateTime.now(), null, null, validRestaurant, dishOrders,null);


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
        OrderRestaurant orderRestaurant = new OrderRestaurant(1L, 1L, LocalDateTime.now(), null, null, validRestaurant, dishOrders,null);


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
        OrderRestaurant orderRestaurant = new OrderRestaurant(1L, 1L, LocalDateTime.now(), null, null, validRestaurant, dishOrders,null);


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
        OrderRestaurant orderRestaurant = new OrderRestaurant(1L, 1L, LocalDateTime.now(), null, null, validRestaurant, dishOrders,null);


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
        OrderStatus state = OrderStatus.FINISHED_ORDER;
        int page = 0;
        int size = 10;

        Restaurant restaurant = new Restaurant(1L, "Mock", "ABC", "1", "321312312", "", "", new HashSet<>());
        OrderRestaurant orderRestaurant = new OrderRestaurant(1L, 111L, LocalDateTime.now(), state, 5L, restaurant, List.of(),null);
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
        Page<OrderRestaurant> result = restaurantUseCase.getOrdersPage(employeeId, state, page, size);

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
        OrderStatus state = OrderStatus.FINISHED_ORDER;
        int page = 0;
        int size = 10;

        // Mock restaurantPersistencePort
        when(restaurantPersistencePort.getRestaurantIdByEmployeeId(employeeId)).thenReturn(Optional.empty());

        // Act and assert
        assertThrows(RestaurantNotExistsException.class, () ->
                restaurantUseCase.getOrdersPage(employeeId, state, page, size));

        verify(restaurantPersistencePort, times(1)).getRestaurantIdByEmployeeId(employeeId);
        verifyNoInteractions(orderRestaurantPersistencePort);
        verifyNoInteractions(dishOrderPersistencePort);
    }

    @Test
    void getOrdersList_EmptyDishOrders_ShouldThrowMalformedOrderException() {
        // Arrange
        Long employeeId = 123L;
        OrderStatus state = OrderStatus.EARRING_ORDER;
        int page = 0;
        int size = 10;
        Long restaurantId = 456L;

        Restaurant restaurant = new Restaurant(1L, "Mock", "ABC", "1", "321312312", "", "", new HashSet<>());
        OrderRestaurant orderRestaurant = new OrderRestaurant(1L, 111L, LocalDateTime.now(), state, 5L, restaurant, List.of(),null);

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
                restaurantUseCase.getOrdersPage(employeeId, state, page, size));

        verify(restaurantPersistencePort, times(1)).getRestaurantIdByEmployeeId(employeeId);
        verify(orderRestaurantPersistencePort, times(1)).getOrdersList(restaurantId, state, page, size);
        verify(dishOrderPersistencePort, times(1)).getDishOrderByOrderRestaurantId(anyLong());
    }

    @Test
    void assignEmployeeToOrder_InvalidData_ThrowsOrderStatusNotAllowedForThisActionException() {
        // Arrange
        String employeeId = "123";
        Long restaurantId = 1L;
        List<Long> ordersId = Arrays.asList(1L, 2L, 3L);

        List<OrderRestaurant> restaurantOrdersList = createOrderRestaurantList(restaurantId, ordersId);
        List<OrderRestaurant> selectedOrdersRestaurant = createOrderRestaurantListWithWrongOrderStatusState(restaurantId, ordersId);


        when(restaurantPersistencePort.getRestaurantIdByEmployeeId(anyLong()))
                .thenReturn(Optional.of(restaurantId));
        when(orderRestaurantPersistencePort.getOrdersList(restaurantId)).thenReturn(restaurantOrdersList);
        when(orderRestaurantPersistencePort.getOrdersById(ordersId)).thenReturn(Optional.of(selectedOrdersRestaurant));

        // Act
        assertThrows(OrderStatusNotAllowedForThisActionException.class,() -> restaurantUseCase.assignEmployeeToOrder(employeeId, ordersId));

        // Assert

        verify(orderRestaurantPersistencePort, times(1)).getOrdersById(ordersId);
        verify(orderRestaurantPersistencePort, never()).saveAllOrderRestaurant(selectedOrdersRestaurant);

    }



    @Test
    void assignEmployeeToOrder_ValidData_SuccessfullyAssigned() {
        // Arrange
        String employeeId = "123";
        Long restaurantId = 1L;
        List<Long> ordersId = Arrays.asList(1L, 2L, 3L);

        List<OrderRestaurant> restaurantOrdersList = createOrderRestaurantList(restaurantId, ordersId);
        List<OrderRestaurant> selectedOrdersRestaurant = createOrderRestaurantList(restaurantId, ordersId);

        when(restaurantPersistencePort.getRestaurantIdByEmployeeId(anyLong()))
                .thenReturn(Optional.of(restaurantId));
        when(orderRestaurantPersistencePort.getOrdersList(restaurantId)).thenReturn(restaurantOrdersList);
        when(orderRestaurantPersistencePort.getOrdersById(ordersId)).thenReturn(Optional.of(selectedOrdersRestaurant));

        // Act
        assertDoesNotThrow(() -> restaurantUseCase.assignEmployeeToOrder(employeeId, ordersId));

        // Assert

        verify(orderRestaurantPersistencePort, times(1)).getOrdersById(ordersId);
        verify(orderRestaurantPersistencePort, times(1)).saveAllOrderRestaurant(selectedOrdersRestaurant);

    }

    @Test
    void assignEmployeeToOrder_InvalidEmployeeId_ThrowsEmployeeNotFindException() {
        // Arrange
        String employeeId = "1";
        List<Long> ordersId = Arrays.asList(1L, 2L, 3L);

        when(restaurantPersistencePort.getRestaurantIdByEmployeeId(anyLong()))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EmployeeNotFindException.class,
                () -> restaurantUseCase.assignEmployeeToOrder(employeeId, ordersId));

        verify(restaurantPersistencePort, times(1)).getRestaurantIdByEmployeeId(anyLong());
        verify(orderRestaurantPersistencePort, never()).getOrdersList(anyLong());
        verify(orderRestaurantPersistencePort, never()).getOrdersById(anyList());
        verify(orderRestaurantPersistencePort, never()).saveAllOrderRestaurant(anyList());
    }

    @Test
    void assignEmployeeToOrder_InvalidOrdersId_ThrowsOrdersNotFoundException() {
        // Arrange
        String employeeId = "123";
        Long restaurantId = 1L;
        List<Long> ordersId = Arrays.asList(1L, 2L, 3L);

        List<OrderRestaurant> restaurantOrdersList = createOrderRestaurantList(restaurantId, ordersId);

        when(restaurantPersistencePort.getRestaurantIdByEmployeeId(anyLong()))
                .thenReturn(Optional.of(restaurantId));
        when(orderRestaurantPersistencePort.getOrdersList(restaurantId)).thenReturn(restaurantOrdersList);
        when(orderRestaurantPersistencePort.getOrdersById(ordersId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(OrdersNotFoundException.class,
                () -> restaurantUseCase.assignEmployeeToOrder(employeeId, ordersId));

        verify(restaurantPersistencePort, times(1)).getRestaurantIdByEmployeeId(anyLong());
        verify(orderRestaurantPersistencePort, times(1)).getOrdersList(anyLong());
        verify(orderRestaurantPersistencePort, times(1)).getOrdersById(anyList());
        verify(orderRestaurantPersistencePort, never()).saveAllOrderRestaurant(anyList());
    }

    @Test
    void assignEmployeeToOrder_NotFoundAllOrders_ThrowsOrdersNotFoundException() {
        // Arrange
        String employeeId = "123";
        Long restaurantId = 1L;
        List<Long> ordersIds = Arrays.asList(1L, 2L);
        List<Long> selectedOrdersIds = Arrays.asList(1L, 2L,3L);

        List<OrderRestaurant> restaurantOrdersList = createOrderRestaurantList(restaurantId, ordersIds);
        List<OrderRestaurant> selectedOrdersList = createOrderRestaurantList(restaurantId, selectedOrdersIds);

        when(restaurantPersistencePort.getRestaurantIdByEmployeeId(anyLong()))
                .thenReturn(Optional.of(restaurantId));
        when(orderRestaurantPersistencePort.getOrdersList(restaurantId)).thenReturn(restaurantOrdersList);
        when(orderRestaurantPersistencePort.getOrdersById(selectedOrdersIds)).thenReturn(Optional.of(selectedOrdersList));

        // Act & Assert
        assertThrows(OrdersNotFoundException.class,
                () -> restaurantUseCase.assignEmployeeToOrder(employeeId, selectedOrdersIds));

        verify(restaurantPersistencePort, times(1)).getRestaurantIdByEmployeeId(anyLong());
        verify(orderRestaurantPersistencePort, times(1)).getOrdersList(anyLong());
        verify(orderRestaurantPersistencePort, times(1)).getOrdersById(anyList());
        verify(orderRestaurantPersistencePort, never()).saveAllOrderRestaurant(anyList());
    }

    private List<OrderRestaurant> createOrderRestaurantList(Long restaurantId, List<Long> orderRestaurantIds) {

        List<OrderRestaurant> orderRestaurantList = new ArrayList<>();
        Restaurant restaurant = new Restaurant();
        restaurant.setId(restaurantId);

        orderRestaurantIds.forEach(
                id -> {
                    OrderRestaurant orderRestaurant = new OrderRestaurant();
                    orderRestaurant.setId(id);
                    orderRestaurant.setOrderStatus(OrderStatus.EARRING_ORDER);
                    orderRestaurant.setRestaurant(restaurant);
                    orderRestaurantList.add(orderRestaurant);
                });
        return orderRestaurantList;
    }

    private List<OrderRestaurant> createOrderRestaurantListWithWrongOrderStatusState(Long restaurantId, List<Long> orderRestaurantIds) {

        List<OrderRestaurant> orderRestaurantList = new ArrayList<>();
        Restaurant restaurant = new Restaurant();
        restaurant.setId(restaurantId);

        orderRestaurantIds.forEach(
                id -> {
                    OrderRestaurant orderRestaurant = new OrderRestaurant();
                    orderRestaurant.setId(id);
                    orderRestaurant.setOrderStatus(OrderStatus.READY_ORDER);
                    orderRestaurant.setRestaurant(restaurant);
                    orderRestaurantList.add(orderRestaurant);
                });
        return orderRestaurantList;
    }
    @Test
    void testOrderRestaurantReady_ValidOrder() {
        // Arrange
        Long orderRestaurantId = 1L;
        OrderRestaurant orderRestaurant = new OrderRestaurant();
        orderRestaurant.setId(orderRestaurantId);
        orderRestaurant.setIdChef(123L);
        orderRestaurant.setIdClient(456L);

        String phoneNumber = "+573123456789";

        when(orderRestaurantPersistencePort.getOrderById(orderRestaurantId)).thenReturn(Optional.of(orderRestaurant));
        when(userClient.getUserPhoneNumber(orderRestaurant.getIdClient().toString())).thenReturn(phoneNumber);

        // Act
        assertDoesNotThrow(() -> restaurantUseCase.orderRestaurantReady(orderRestaurantId));

        // Assert
        assertEquals(OrderStatus.READY_ORDER, orderRestaurant.getOrderStatus());
        assertNotNull(orderRestaurant.getVerificationCode());
        verify(orderRestaurantPersistencePort, times(1)).saveOrderRestaurant(orderRestaurant);
        verify(smsClient, times(1)).sendMessage(anyString(), anyString());
    }

    @Test
    void testOrderRestaurantReady_InvalidOrder() {
        // Arrange
        Long orderRestaurantId = 1L;
        OrderRestaurant orderRestaurant = new OrderRestaurant();
        orderRestaurant.setId(orderRestaurantId);
        orderRestaurant.setIdChef(null);

        when(orderRestaurantPersistencePort.getOrderById(orderRestaurantId)).thenReturn(Optional.of(orderRestaurant));

        // Act & Assert
        assertThrows(OrderNotAssignedException.class, () -> restaurantUseCase.orderRestaurantReady(orderRestaurantId));
        verify(orderRestaurantPersistencePort, never()).saveOrderRestaurant(orderRestaurant);
        verify(smsClient, never()).sendMessage(anyString(), anyString());
    }

    @Test
    void testOrderRestaurantReady_OrderNotFound() {
        // Arrange
        Long orderRestaurantId = 1L;

        when(orderRestaurantPersistencePort.getOrderById(orderRestaurantId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(OrdersNotFoundException.class, () -> restaurantUseCase.orderRestaurantReady(orderRestaurantId));
        verify(orderRestaurantPersistencePort, never()).saveOrderRestaurant(any(OrderRestaurant.class));
        verify(smsClient, never()).sendMessage(anyString(), anyString());
    }

    @Test
    void orderRestaurantDeliver_ValidOrder_AllStatusChecksPassed() {
        // Arrange
        Long orderRestaurantId = 1L;
        String verificationCode = "123456";
        Long employeeId = 100L;

        OrderRestaurant orderRestaurant = new OrderRestaurant();
        orderRestaurant.setId(orderRestaurantId);
        orderRestaurant.setIdChef(employeeId);
        orderRestaurant.setOrderStatus(OrderStatus.READY_ORDER);
        orderRestaurant.setVerificationCode(verificationCode);

        when(orderRestaurantPersistencePort.getOrderById(orderRestaurantId)).thenReturn(Optional.of(orderRestaurant));

        // Act
        restaurantUseCase.orderRestaurantDeliver(orderRestaurantId, verificationCode, employeeId);

        // Assert
        assertEquals(OrderStatus.FINISHED_ORDER, orderRestaurant.getOrderStatus());
        verify(traceabilityClient, times(1)).modifyOrderTrace(orderRestaurant);
        verify(orderRestaurantPersistencePort, times(1)).saveOrderRestaurant(orderRestaurant);
    }

    @Test
    void orderRestaurantDeliver_InvalidChef_ThrowsOrderNotAssignedException() {
        // Arrange
        Long orderRestaurantId = 1L;
        String verificationCode = "123456";
        Long employeeId = 100L;

        OrderRestaurant orderRestaurant = new OrderRestaurant();
        orderRestaurant.setId(orderRestaurantId);
        orderRestaurant.setIdChef(200L);
        orderRestaurant.setOrderStatus(OrderStatus.READY_ORDER);
        orderRestaurant.setVerificationCode(verificationCode);

        when(orderRestaurantPersistencePort.getOrderById(orderRestaurantId)).thenReturn(Optional.of(orderRestaurant));

        // Assert
        assertThrows(OrderNotAssignedException.class, () -> {
            // Act
            restaurantUseCase.orderRestaurantDeliver(orderRestaurantId, verificationCode, employeeId);
        });
        verify(traceabilityClient, never()).modifyOrderTrace(any());
        verify(orderRestaurantPersistencePort, never()).saveOrderRestaurant(any());
    }

    @Test
    void orderRestaurantDeliver_OrderNotReady_ThrowsNotAReadyOrderException() {
        // Arrange
        Long orderRestaurantId = 1L;
        String verificationCode = "123456";
        Long employeeId = 100L;

        OrderRestaurant orderRestaurant = new OrderRestaurant();
        orderRestaurant.setId(orderRestaurantId);
        orderRestaurant.setIdChef(employeeId);
        orderRestaurant.setOrderStatus(OrderStatus.IN_PREPARATION_ORDER);
        orderRestaurant.setVerificationCode(verificationCode);

        when(orderRestaurantPersistencePort.getOrderById(orderRestaurantId)).thenReturn(Optional.of(orderRestaurant));

        // Assert
        assertThrows(NotAReadyOrderException.class, () -> {
            // Act
            restaurantUseCase.orderRestaurantDeliver(orderRestaurantId, verificationCode, employeeId);
        });
        verify(traceabilityClient, never()).modifyOrderTrace(any());
        verify(orderRestaurantPersistencePort, never()).saveOrderRestaurant(any());
    }

    @Test
    void orderRestaurantDeliver_WrongVerificationCode_ThrowsWrongVerificationCodeException() {
        // Arrange
        Long orderRestaurantId = 1L;
        String verificationCode = "123456";
        Long employeeId = 100L;

        OrderRestaurant orderRestaurant = new OrderRestaurant();
        orderRestaurant.setId(orderRestaurantId);
        orderRestaurant.setIdChef(employeeId);
        orderRestaurant.setOrderStatus(OrderStatus.READY_ORDER);
        orderRestaurant.setVerificationCode("654321"); // Wrong verification code

        when(orderRestaurantPersistencePort.getOrderById(orderRestaurantId)).thenReturn(Optional.of(orderRestaurant));

        // Assert
        assertThrows(WrongVerificationCodeException.class, () -> {
            // Act
            restaurantUseCase.orderRestaurantDeliver(orderRestaurantId, verificationCode, employeeId);
        });
        verify(traceabilityClient, never()).modifyOrderTrace(any());
        verify(orderRestaurantPersistencePort, never()).saveOrderRestaurant(any());
    }
    @Test
    void cancelOrder_WithValidOrderAndUser_ShouldUpdateOrderStatusAndSaveOrderRestaurant() {
        // Arrange
        Long userId = 4L;
        Long orderId = 2L;
        OrderRestaurant orderRestaurant = new OrderRestaurant();
        orderRestaurant.setId(orderId);
        orderRestaurant.setIdClient(userId);
        orderRestaurant.setOrderStatus(OrderStatus.EARRING_ORDER);

        when(orderRestaurantPersistencePort.getOrderById(orderId)).thenReturn(Optional.of(orderRestaurant));

        // Act
        assertDoesNotThrow(() -> restaurantUseCase.cancelOrder(userId, orderId));

        // Assert
        assertEquals(OrderStatus.CANCELED_ORDER, orderRestaurant.getOrderStatus());
        verify(orderRestaurantPersistencePort, times(1)).saveOrderRestaurant(orderRestaurant);
        verify(traceabilityClient, times(1)).modifyOrderTrace(orderRestaurant);
    }

    @Test
    void cancelOrder_WithInvalidUser_ShouldThrowOrderNotFoundException() {
        // Arrange
        Long userId = 10L;
        Long orderId = 2L;
        OrderRestaurant orderRestaurant = new OrderRestaurant();
        orderRestaurant.setId(orderId);
        orderRestaurant.setIdClient(4L);
        orderRestaurant.setOrderStatus(OrderStatus.EARRING_ORDER);

        when(orderRestaurantPersistencePort.getOrderById(orderId)).thenReturn(Optional.of(orderRestaurant));

        // Act & Assert
        assertThrows(OrderNotFoundException.class, () -> restaurantUseCase.cancelOrder(userId, orderId));
        assertEquals(OrderStatus.EARRING_ORDER, orderRestaurant.getOrderStatus());
        verify(orderRestaurantPersistencePort, never()).saveOrderRestaurant(any());
        verify(traceabilityClient, never()).modifyOrderTrace(any());
    }

    @Test
    void cancelOrder_WithInvalidOrderStatus_ShouldThrowOrderStatusNotAllowedForThisActionException() {
        // Arrange
        Long userId = 4L;
        Long orderId = 2L;
        OrderRestaurant orderRestaurant = new OrderRestaurant();
        orderRestaurant.setId(orderId);
        orderRestaurant.setIdClient(userId);
        orderRestaurant.setOrderStatus(OrderStatus.READY_ORDER);

        when(orderRestaurantPersistencePort.getOrderById(orderId)).thenReturn(Optional.of(orderRestaurant));

        // Act & Assert
        assertThrows(OrderStatusNotAllowedForThisActionException.class, () -> restaurantUseCase.cancelOrder(userId, orderId));
        assertEquals(OrderStatus.READY_ORDER, orderRestaurant.getOrderStatus());
        verify(orderRestaurantPersistencePort, never()).saveOrderRestaurant(any());
        verify(traceabilityClient, never()).modifyOrderTrace(any());
    }
    @Test
    void testHistoryOrder_ValidOrder_ReturnsTraceabilityList() {
        // Arrange
        Long userId = 1L;
        Long orderId = 2L;

        OrderRestaurant orderRestaurant = new OrderRestaurant();
        orderRestaurant.setId(orderId);
        orderRestaurant.setIdClient(userId);

        List<Traceability> traces = new ArrayList<>();
        traces.add(new Traceability());
        traces.add(new Traceability());

        when(orderRestaurantPersistencePort.getOrderById(orderId)).thenReturn(Optional.of(orderRestaurant));
        when(traceabilityClient.getOrderTrace(orderId)).thenReturn(traces);

        // Act
        List<Traceability> result = restaurantUseCase.historyOrder(userId, orderId);

        // Assert
        assertEquals(traces, result);
        verify(orderRestaurantPersistencePort, times(1)).getOrderById(orderId);
        verify(traceabilityClient, times(1)).getOrderTrace(orderId);
    }

    @Test
    void testHistoryOrder_InvalidOrder_ThrowsOrderNotFoundException() {
        // Arrange
        Long userId = 1L;
        Long orderId = 2L;

        when(orderRestaurantPersistencePort.getOrderById(orderId)).thenReturn(Optional.empty());

        // Act
        assertThrows(OrderNotFoundException.class, () -> restaurantUseCase.historyOrder(userId, orderId));

        // Assert
        verify(orderRestaurantPersistencePort, times(1)).getOrderById(orderId);
        verify(traceabilityClient, never()).getOrderTrace(orderId);
    }

    @Test
    void testHistoryOrder_UnauthorizedUser_ThrowsOrderNotFoundException() {
        // Arrange
        Long userId = 1L;
        Long orderId = 2L;

        OrderRestaurant orderRestaurant = new OrderRestaurant();
        orderRestaurant.setId(orderId);
        orderRestaurant.setIdClient(3L);

        when(orderRestaurantPersistencePort.getOrderById(orderId)).thenReturn(Optional.of(orderRestaurant));

        // Act
        assertThrows(OrderNotFoundException.class, () -> restaurantUseCase.historyOrder(userId, orderId));

        // Assert
        verify(orderRestaurantPersistencePort, times(1)).getOrderById(orderId);
        verify(traceabilityClient, never()).getOrderTrace(orderId);
    }

    @Test
    void testHistoryOrder_NoTraces_ThrowsOrdersNotFoundException() {
        // Arrange
        Long userId = 1L;
        Long orderId = 2L;

        OrderRestaurant orderRestaurant = new OrderRestaurant();
        orderRestaurant.setId(orderId);
        orderRestaurant.setIdClient(userId);

        when(orderRestaurantPersistencePort.getOrderById(orderId)).thenReturn(Optional.of(orderRestaurant));
        when(traceabilityClient.getOrderTrace(orderId)).thenReturn(new ArrayList<>());

        // Act
        assertThrows(OrdersNotFoundException.class, () -> restaurantUseCase.historyOrder(userId, orderId));

        // Assert
        verify(orderRestaurantPersistencePort, times(1)).getOrderById(orderId);
        verify(traceabilityClient, times(1)).getOrderTrace(orderId);
    }
    @Test
    void testRestaurantStatistics_WithValidData_ReturnsStatistics() {
        // Arrange
        Long restaurantId = 1L;
        Long userId = 123L;
        Restaurant restaurant = new Restaurant();
        restaurant.setIdProprietary(userId.toString());

        Set<String> employeesIds = new HashSet<>(List.of("2","3","4"));
        restaurant.setEmployees(employeesIds);

        when(restaurantPersistencePort.getRestaurant(restaurantId)).thenReturn(Optional.of(restaurant));

        List<EmployeeStatistics> employeeStatistics = Arrays.asList(
                new EmployeeStatistics(1L,2L, Duration.ofMinutes(30)),
                new EmployeeStatistics(2L,3L, Duration.ofMinutes(45)),
                new EmployeeStatistics(3L,4L, Duration.ofMinutes(60)));

        when(traceabilityClient.getEmployeeStatistics(Arrays.asList(2L, 3L, 4L)))
                .thenReturn(employeeStatistics);


        List<Long> ordersIds = Arrays.asList(1L, 2L, 3L);
        List<OrderRestaurant> orderRestaurantList = createOrderRestaurantList(restaurantId,ordersIds);

        List<OrderStatistics> orderStatistics = Arrays.asList(
                new OrderStatistics(1L, Duration.ofMinutes(20)),
                new OrderStatistics(2L, Duration.ofMinutes(40)),
                new OrderStatistics(3L, Duration.ofMinutes(60)));

        when(orderRestaurantPersistencePort.getOrdersListWithStatus(restaurantId, OrderStatus.FINISHED_ORDER))
                .thenReturn(orderRestaurantList);
        when(traceabilityClient.getOrdersStatistics(ordersIds))
                .thenReturn(orderStatistics);

        // Act
        RestaurantStatistics statistics = restaurantUseCase.restaurantStatistics(userId, restaurantId);

        // Assertions
        assertEquals(employeeStatistics, statistics.getEmployeeStatistics());
        assertEquals(orderStatistics, statistics.getOrderStatistics());
    }

    @Test
    void testRestaurantStatistics_WithInvalidUser_ThrowsException() {
        // Arrange
        Long restaurantId = 1L;
        Long userId = 123L;
        Restaurant restaurant = new Restaurant();
        restaurant.setIdProprietary("456");

        when(restaurantPersistencePort.getRestaurant(restaurantId)).thenReturn(Optional.of(restaurant));

        // AssertThrow
        assertThrows(NotProprietaryGivenRestaurantException.class,
                () -> restaurantUseCase.restaurantStatistics(userId, restaurantId));
    }

    @Test
    void testRestaurantStatistics_WithNonExistingRestaurant_ThrowsException() {
        // Arrange
        Long restaurantId = 1L;
        Long userId = 123L;

        when(restaurantPersistencePort.getRestaurant(restaurantId)).thenReturn(Optional.empty());

        // AssertThrow
        assertThrows(RestaurantNotExistsException.class,
                () -> restaurantUseCase.restaurantStatistics(userId, restaurantId));
    }
}
