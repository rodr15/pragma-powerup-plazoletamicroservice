package com.ti.acelera.plazoletamicroservice.adapters.http.dto.response;

import com.ti.acelera.plazoletamicroservice.domain.model.OrderStatus;
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
    private Long idChef;
    private LocalDate date;
    private OrderStatus orderStatus;
    private List<DishOrderResponseDto> dishes;

}
