package com.ti.acelera.plazoletamicroservice.domain.gateway;

import com.ti.acelera.plazoletamicroservice.domain.model.*;

import java.util.List;

public interface ITraceabilityClient {

    void saveOrderTrace( OrderRestaurant order );
    void modifyOrderTrace(OrderRestaurant orderRestaurant);
    List<Traceability> getOrderTrace(Long orderId);
    List<EmployeeStatistics> getEmployeeStatistics(List<Long> employeesId);
    List<OrderStatistics> getOrdersStatistics(List<Long> ordersId);
    void saveRestaurantTrace(RestaurantObjectsTrace restaurantObjectsTrace);
}
