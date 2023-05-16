package com.ti.acelera.plazoletamicroservice.adapters.http.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UpdateDishRequestDto {
    private Long price;
    private String description;

    public static final String example = "{" +
            "\"description\":\"Pizza\"," +
            "\"price\":\"12345\"" +
            "}";

}
