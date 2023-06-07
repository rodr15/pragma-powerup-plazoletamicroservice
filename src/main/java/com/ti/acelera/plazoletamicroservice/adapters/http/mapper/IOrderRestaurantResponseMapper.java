package com.ti.acelera.plazoletamicroservice.adapters.http.mapper;

import com.ti.acelera.plazoletamicroservice.adapters.http.dto.response.DishOrderResponseDto;
import com.ti.acelera.plazoletamicroservice.adapters.http.dto.response.OrderRestaurantResponseDto;
import com.ti.acelera.plazoletamicroservice.domain.model.DishOrder;
import com.ti.acelera.plazoletamicroservice.domain.model.OrderRestaurant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IOrderRestaurantResponseMapper {
    @Mapping(target = "id",source = "id")
    OrderRestaurantResponseDto toOrderRestaurantResponseDto(OrderRestaurant orderRestaurant);
    @Mapping(target = "dishId",source = "dish.id")
    @Mapping(target = "name",source = "dish.name")
    DishOrderResponseDto toDishOrderResponseDto(DishOrder dishOrder);
}
