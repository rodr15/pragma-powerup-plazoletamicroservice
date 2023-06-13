package com.ti.acelera.plazoletamicroservice.adapters.driver.client.adapter;

import com.ti.acelera.plazoletamicroservice.adapters.driver.client.dto.UserRoleDto;
import com.ti.acelera.plazoletamicroservice.adapters.driver.client.exceptions.UserNotFoundException;
import com.ti.acelera.plazoletamicroservice.domain.gateway.IUserClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;


public class UserClientImpl implements IUserClient {
    @Value("${user.service.url}")
    private String userServiceUrl;

    public String getRoleByDni(String userDni) {
        RestTemplate restTemplate = new RestTemplate();
        try {
            String url = String.format("%s/user-role/%s", userServiceUrl, userDni);
            UserRoleDto userRoleDto = restTemplate.getForObject(url, UserRoleDto.class);
            if (userRoleDto == null) {
                throw new UserNotFoundException();
            }
            return userRoleDto.getName();
        } catch (Exception e) {
            throw new UserNotFoundException();
        }

    }

    @Override
    public String getUserPhoneNumber(String userDni) {
        RestTemplate restTemplate = new RestTemplate();
        try {
            String url = String.format("%s/user-phone/%s", userServiceUrl, userDni);
            String userPhone = restTemplate.getForObject(url, String.class);
            if (userPhone == null) {
                throw new UserNotFoundException();
            }
            return userPhone;
        } catch (Exception e) {
            throw new UserNotFoundException();
        }
    }


}



