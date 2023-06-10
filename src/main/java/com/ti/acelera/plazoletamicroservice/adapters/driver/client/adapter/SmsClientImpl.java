package com.ti.acelera.plazoletamicroservice.adapters.driver.client.adapter;

import com.ti.acelera.plazoletamicroservice.domain.gateway.ISmsClient;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;

public class SmsClientImpl implements ISmsClient {
    public static final String ACCOUNT_SID = "AC6f7c3755e94af14295c6340b3e4a3285";
    public static final String AUTH_TOKEN = "1eb0fc0ee4347c269f47f754756043d3";


    @Override
    public void sendMessage(String clientNumber,String message ) {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        Message.creator(
                new com.twilio.type.PhoneNumber(clientNumber),
                new com.twilio.type.PhoneNumber("+13614410715"),
                message)
                .create();
    }

}



