package com.ti.acelera.plazoletamicroservice.adapters.http.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderRestaurantResponseDto {
    private Long id;
    private Long idClient;
    private LocalDate date;
    private String state;
    private List<DishOrderResponseDto> dishes;

}
