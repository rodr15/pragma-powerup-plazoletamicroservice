package com.ti.acelera.plazoletamicroservice.adapters.http.mapper;

import com.ti.acelera.plazoletamicroservice.adapters.http.dto.request.DishRequestDto;
import com.ti.acelera.plazoletamicroservice.domain.model.Dish;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IDishRequestMapper {
    @Mapping(target = "restaurant.id", source = "idRestaurant")
    Dish toDish(DishRequestDto dishRequestDto);
}



