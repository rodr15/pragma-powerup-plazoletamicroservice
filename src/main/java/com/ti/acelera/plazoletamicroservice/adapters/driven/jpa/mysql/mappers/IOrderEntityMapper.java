package com.ti.acelera.plazoletamicroservice.adapters.driven.jpa.mysql.mappers;
import com.ti.acelera.plazoletamicroservice.adapters.driven.jpa.mysql.entity.OrderRestaurantEntity;
import com.ti.acelera.plazoletamicroservice.domain.model.OrderRestaurant;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IOrderEntityMapper {
    OrderRestaurant toOrder(OrderRestaurantEntity orderEntity);
    OrderRestaurantEntity toOrderEntity(OrderRestaurant orderRestaurant);


}
