package com.ti.acelera.plazoletamicroservice.adapters.http.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class DishRequestDto {
    @NotBlank(message = "Mandatory")
    private String name;
    @Positive(message = "Must be an id")
    @NotBlank(message = "Mandatory")
    private Long idCategory;
    @NotBlank(message = "Mandatory")
    private String description;
    @Positive(message = "Must be greater than 0")
    @NotNull(message = "Mandatory")
    private Long price;
    @Positive(message = "Must be an id")
    @NotNull(message = "Mandatory")
    private Long idRestaurant;
    @NotBlank(message = "Mandatory")
    private String urlImage;


    public static final String example = "{" +
            "\"name\":\"Pizza\"," +
            "\"idCategory\":\"1\"," +
            "\"description\":\"Pizza\"," +
            "\"price\":\"12345\"," +
            "\"idRestaurant\":\"123123123\"," +
            "\"urlImage\":\"https://mail.google.com/mail/u/0/#inbox\"" +
            "}";

}
