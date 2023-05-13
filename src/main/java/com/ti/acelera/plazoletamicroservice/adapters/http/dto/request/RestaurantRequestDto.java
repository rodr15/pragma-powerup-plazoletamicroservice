package com.ti.acelera.plazoletamicroservice.adapters.http.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class RestaurantRequestDto {

    @Pattern(regexp = "^(?=.*[a-zA-Z])[a-zA-Z\\d]+$",message = "It is mandatory and can not contain numbers")
    private String name;
    @NotBlank(message = "Mandatory")
    private String direction;
    @NotBlank(message = "Mandatory")
    private String idProprietary;
    @Pattern(regexp = "^[0-9,+]{7,13}$",message = "Mandatory")
    private String phone;
    @NotBlank(message = "Mandatory")
    private String urlLogo;
    @Pattern(regexp = "^([0-9])*$",message = "Mandatory")
    private String nit;



    public static final String example = "{" +
            "\"name\":\"JohnPizza\"," +
            "\"direction\":\"CC Santa\"," +
            "\"idProprietary\":\"12312312\"," +
            "\"phone\":\"+573000000000\"," +
            "\"urlLogo\":\"https://mail.google.com/mail/u/0/#inbox\"," +
            "\"nit\":\"123123123\"" +
            "}";
}
