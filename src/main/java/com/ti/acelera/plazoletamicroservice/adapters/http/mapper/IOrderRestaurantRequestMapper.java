package com.ti.acelera.plazoletamicroservice.adapters.http.mapper;

import com.ti.acelera.plazoletamicroservice.adapters.http.dto.request.DishOrderRequestDto;
import com.ti.acelera.plazoletamicroservice.adapters.http.dto.request.OrderRequestDto;
import com.ti.acelera.plazoletamicroservice.domain.model.DishOrder;
import com.ti.acelera.plazoletamicroservice.domain.model.OrderRestaurant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IOrderRestaurantRequestMapper {
    @Mapping(target = "restaurant.id", source = "restaurantId")
    OrderRestaurant toOrderRestaurant(OrderRequestDto orderRequestDto);

    @Mapping(target = "dish.id", source = "dishId")
    DishOrder toDishOrder(DishOrderRequestDto dishOrderRequestDto);
}
