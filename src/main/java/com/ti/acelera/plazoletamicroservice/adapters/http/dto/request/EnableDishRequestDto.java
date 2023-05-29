package com.ti.acelera.plazoletamicroservice.adapters.http.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class EnableDishRequestDto {

    private Long dishId;
    private boolean state;

    public static final String example = "{" +
            "\"dishId\":\"1\"," +
            "\"state\":\"true\"" +
            "}";

}
