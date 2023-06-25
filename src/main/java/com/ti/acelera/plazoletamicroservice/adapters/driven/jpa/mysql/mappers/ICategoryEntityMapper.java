package com.ti.acelera.plazoletamicroservice.adapters.driven.jpa.mysql.mappers;

import com.ti.acelera.plazoletamicroservice.adapters.driven.jpa.mysql.entity.CategoryEntity;
import com.ti.acelera.plazoletamicroservice.domain.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface ICategoryEntityMapper {
    Category toCategory (CategoryEntity category);
}
