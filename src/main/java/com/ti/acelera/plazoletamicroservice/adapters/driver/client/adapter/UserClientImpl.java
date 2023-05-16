package com.ti.acelera.plazoletamicroservice.adapters.driver.client.adapter;

import com.ti.acelera.plazoletamicroservice.adapters.driver.client.dto.UserRoleDto;
import com.ti.acelera.plazoletamicroservice.adapters.driver.client.exceptions.UserNotFoundException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;


public class UserClientImpl {


    public String getRoleByDni(String userDni) {
        RestTemplate restTemplate = new RestTemplate();
        try {

            String url = String.format("http://localhost:8090/user/user-role/%s", userDni);

            UserRoleDto userRoleDto = restTemplate.getForObject(url, UserRoleDto.class);
            if (userRoleDto == null) {
                throw new UserNotFoundException();
            }
            return userRoleDto.getName();
        } catch (Exception e) {
            throw new UserNotFoundException();
        }

    }


    public static boolean isAllowed(String url, String token) {
        RestTemplate restTemplate = new RestTemplate();
        try {

            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Bearer " + token );

            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<Boolean> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    Boolean.class
            );

            return response.getBody();

        } catch (Exception e) {
            throw new UserNotFoundException();
        }
    }


    public static void main(String[] args) {
        boolean dummy = isAllowed("http://localhost:8090/restaurant/add","eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxMjMiLCJyb2xlcyI6WyJST0xFX0FETUlOIiwiUk9MRV9BRE1JTiJdLCJpYXQiOjE2ODQyNzY4MzgsImV4cCI6MTY4NDkyNDgzOH0.8k9KUNzQt0vBVIUTpHfOPl1jyrnx0815oRj2YVn80JQ");

        System.out.println(dummy);
    }

}


