package com.ti.acelera.plazoletamicroservice.adapters.http.mapper;

import com.ti.acelera.plazoletamicroservice.adapters.http.dto.response.RestaurantResponseDto;
import com.ti.acelera.plazoletamicroservice.domain.model.Restaurant;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IRestaurantResponseMapper {
    RestaurantResponseDto toResponseDto(Restaurant restaurant);
}
