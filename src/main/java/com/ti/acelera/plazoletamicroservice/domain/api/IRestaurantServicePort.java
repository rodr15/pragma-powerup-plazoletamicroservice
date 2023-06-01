package com.ti.acelera.plazoletamicroservice.domain.api;

import com.ti.acelera.plazoletamicroservice.domain.model.Dish;
import com.ti.acelera.plazoletamicroservice.domain.model.OrderRestaurant;
import com.ti.acelera.plazoletamicroservice.domain.model.Restaurant;
import org.springframework.data.domain.Page;

public interface IRestaurantServicePort {
    OrderRestaurant makeOrder(OrderRestaurant orderRestaurant) ;
    Page<Dish> pageDish( Long restaurantId, Long categoryId, int page,int size );
    Page<Restaurant> pageRestaurants(int page, int size);
    void saveRestaurant(Restaurant restaurant);
    boolean verifyRestaurantOwner(String userId, Long restaurantId);
    void assignEmployee(String userId, Long restaurantId);
}
