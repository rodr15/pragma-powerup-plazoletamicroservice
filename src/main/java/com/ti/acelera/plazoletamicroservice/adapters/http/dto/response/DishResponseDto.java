package com.ti.acelera.plazoletamicroservice.adapters.http.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DishResponseDto {
    private String name;
    private Long idCategory;
    private String description;
    private Long price;
    private String urlImage;
    private Long restaurantId;
}
