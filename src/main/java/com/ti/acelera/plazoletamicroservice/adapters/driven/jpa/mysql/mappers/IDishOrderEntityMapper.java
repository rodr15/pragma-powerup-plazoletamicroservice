package com.ti.acelera.plazoletamicroservice.adapters.driven.jpa.mysql.mappers;

import com.ti.acelera.plazoletamicroservice.adapters.driven.jpa.mysql.entity.DishOrderEntity;
import com.ti.acelera.plazoletamicroservice.domain.model.DishOrder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IDishOrderEntityMapper {
    DishOrder toDishOrder(DishOrderEntity dishOrderEntity);
    @Mapping(target = "orderRestaurant.id", source = "order.id")
    DishOrderEntity toDishOrderEntity(DishOrder dishOrder);
}
