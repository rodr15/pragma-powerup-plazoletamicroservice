package com.ti.acelera.plazoletamicroservice.adapters.http.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantResponseDto {
    private String name;
    private String urlLogo;
}
