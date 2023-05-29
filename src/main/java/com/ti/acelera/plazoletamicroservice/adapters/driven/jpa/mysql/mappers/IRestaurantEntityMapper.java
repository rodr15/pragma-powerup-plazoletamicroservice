package com.ti.acelera.plazoletamicroservice.adapters.driven.jpa.mysql.mappers;

import com.ti.acelera.plazoletamicroservice.adapters.driven.jpa.mysql.entity.RestaurantEntity;
import com.ti.acelera.plazoletamicroservice.domain.model.Restaurant;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IRestaurantEntityMapper {
    RestaurantEntity toEntity(Restaurant restaurant);
    Restaurant toRestaurant(RestaurantEntity restaurant);
}
