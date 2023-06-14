package com.ti.acelera.plazoletamicroservice.domain.gateway;

public interface ISmsClient {
    void sendMessage(String clientNumber,String message);
}
