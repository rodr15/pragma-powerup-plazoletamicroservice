package com.ti.acelera.plazoletamicroservice.adapters.driver.client.adapter;

import com.ti.acelera.plazoletamicroservice.adapters.driver.client.dto.UserRoleDto;
import com.ti.acelera.plazoletamicroservice.adapters.driver.client.exceptions.UserNotFoundException;
import com.ti.acelera.plazoletamicroservice.domain.gateway.IUserClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;


public class UserClientImpl implements IUserClient {


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


}



