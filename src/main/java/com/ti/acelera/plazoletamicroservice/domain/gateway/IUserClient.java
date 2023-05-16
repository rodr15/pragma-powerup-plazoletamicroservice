package com.ti.acelera.plazoletamicroservice.domain.gateway;

public interface IUserClient {

    String getRoleByDni(String userDni);
    boolean isAllowed(String url,String token);

}
