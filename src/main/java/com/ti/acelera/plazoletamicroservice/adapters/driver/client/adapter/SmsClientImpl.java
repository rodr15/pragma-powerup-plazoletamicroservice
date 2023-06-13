package com.ti.acelera.plazoletamicroservice.adapters.driver.client.adapter;

import com.ti.acelera.plazoletamicroservice.domain.gateway.ISmsClient;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import org.springframework.beans.factory.annotation.Value;

public class SmsClientImpl implements ISmsClient {
    @Value("${twillio.accountSid}")
    private String accountSid;
    @Value("${twillio.authToken}")
    private String authToken;
    @Value("${twillio.fromPhone}")
    private String fromPhone;



    @Override
    public void sendMessage(String clientNumber,String message ) {
        Twilio.init(accountSid, authToken);
        Message.creator(
                new com.twilio.type.PhoneNumber(clientNumber),
                new com.twilio.type.PhoneNumber(fromPhone),
                message)
                .create();
    }

}



