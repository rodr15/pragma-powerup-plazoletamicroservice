package com.ti.acelera.plazoletamicroservice.adapters.http.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class DishRequestDto {
    private String name;
    private String idCategory;
    private String description;
    private Long price;
    private Long idRestaurant;
    private String urlImage;


    public static final String example = "{" +
            "\"name\":\"Pizza\"," +
            "\"idCategory\":\"CC Santa\"," +
            "\"description\":\"Pizza\"," +
            "\"price\":\"12345\"," +
            "\"idRestaurant\":\"123123123\"" +
            "\"urlImage\":\"https://mail.google.com/mail/u/0/#inbox\"," +
            "}";

}
