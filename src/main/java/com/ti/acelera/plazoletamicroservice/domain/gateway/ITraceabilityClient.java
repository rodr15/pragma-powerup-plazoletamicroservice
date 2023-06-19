package com.ti.acelera.plazoletamicroservice.domain.gateway;

import com.ti.acelera.plazoletamicroservice.domain.model.OrderRestaurant;

public interface ITraceabilityClient {

    void saveOrderTrace( OrderRestaurant order );
    void modifyOrderTrace(OrderRestaurant orderRestaurant);
}
