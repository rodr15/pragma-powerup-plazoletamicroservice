package com.ti.acelera.plazoletamicroservice.domain.gateway;

import com.ti.acelera.plazoletamicroservice.domain.model.EmployeeStatistics;
import com.ti.acelera.plazoletamicroservice.domain.model.OrderRestaurant;
import com.ti.acelera.plazoletamicroservice.domain.model.OrderStatistics;
import com.ti.acelera.plazoletamicroservice.domain.model.Traceability;

import java.util.List;

public interface ITraceabilityClient {

    void saveOrderTrace( OrderRestaurant order );
    void modifyOrderTrace(OrderRestaurant orderRestaurant);
    List<Traceability> getOrderTrace(Long orderId);
    List<EmployeeStatistics> getEmployeeStatistics(List<Long> employeesId);
    List<OrderStatistics> getOrdersStatistics(List<Long> ordersId);
}
