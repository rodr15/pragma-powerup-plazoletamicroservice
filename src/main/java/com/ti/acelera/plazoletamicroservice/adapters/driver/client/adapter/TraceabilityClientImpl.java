package com.ti.acelera.plazoletamicroservice.adapters.driver.client.adapter;

import com.nimbusds.jose.shaded.gson.JsonObject;
import com.ti.acelera.plazoletamicroservice.adapters.driver.client.exceptions.ServiceNotFoundException;
import com.ti.acelera.plazoletamicroservice.domain.gateway.ITraceabilityClient;
import com.ti.acelera.plazoletamicroservice.domain.model.OrderRestaurant;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

public class TraceabilityClientImpl implements ITraceabilityClient {
    @Value("${traceability.service.url}")
    private String traceabilityServiceUrl;

    @Override
    public void saveOrderTrace(OrderRestaurant order) {
        RestTemplate restTemplate = new RestTemplate();

        try {
            String url = String.format("%s", traceabilityServiceUrl);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            JsonObject orderJsonObject = new JsonObject();
            orderJsonObject.addProperty( "orderId", order.getId() );
            orderJsonObject.addProperty( "employeeId", order.getIdChef() );
            orderJsonObject.addProperty( "clientId", order.getIdClient() );


            HttpEntity<String> request =
                    new HttpEntity<>(orderJsonObject.toString(), headers);

            restTemplate.postForObject(url,request,String.class);

        } catch (Exception e) {
            throw new ServiceNotFoundException(" traceability service not found ");
        }

    }


}
