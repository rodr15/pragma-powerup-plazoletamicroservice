package com.ti.acelera.plazoletamicroservice.adapters.driver.client.adapter;

import com.nimbusds.jose.shaded.gson.JsonObject;
import com.ti.acelera.plazoletamicroservice.adapters.driver.client.exceptions.ServiceNotFoundException;
import com.ti.acelera.plazoletamicroservice.domain.gateway.ITraceabilityClient;
import com.ti.acelera.plazoletamicroservice.domain.model.OrderRestaurant;
import com.ti.acelera.plazoletamicroservice.domain.model.Traceability;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static com.ti.acelera.plazoletamicroservice.configuration.Constants.TRACEABILITY_SERVICE_ERROR;

@RequiredArgsConstructor
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
            orderJsonObject.addProperty("orderId", order.getId());
            orderJsonObject.addProperty("employeeId", order.getIdChef());
            orderJsonObject.addProperty("clientId", order.getIdClient());
            orderJsonObject.addProperty("createdAt", order.getDate().toString());
            orderJsonObject.addProperty("currentState", order.getOrderStatus().toString());

            HttpEntity<String> request =
                    new HttpEntity<>(orderJsonObject.toString(), headers);

            restTemplate.postForObject(url, request, String.class);

        } catch (Exception e) {
            throw new ServiceNotFoundException(TRACEABILITY_SERVICE_ERROR);
        }

    }

    @Override
    public void modifyOrderTrace(OrderRestaurant order) {
        RestTemplate restTemplate = new RestTemplate();

        try {
            String url = String.format("%s", traceabilityServiceUrl);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            JsonObject orderJsonObject = new JsonObject();

            orderJsonObject.addProperty("orderId", order.getId());
            orderJsonObject.addProperty("updatedAt", LocalDateTime.now().toString());
            orderJsonObject.addProperty("currentState", order.getOrderStatus().toString());

            HttpEntity<String> request =
                    new HttpEntity<>(orderJsonObject.toString(), headers);

            restTemplate.put(url, request, String.class);

        } catch (Exception e) {
            throw new ServiceNotFoundException(TRACEABILITY_SERVICE_ERROR);
        }
    }

    @Override
    public List<Traceability> getOrderTrace(Long orderId) {
        RestTemplate restTemplate = new RestTemplate();

        try {

            String url = String.format("%s?orderId=%s", traceabilityServiceUrl, orderId);
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

            HttpEntity<?> entity = new HttpEntity<>(headers);

            ResponseEntity<List<Traceability>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<List<Traceability>>() {});
            return response.getBody();

        } catch (Exception e) {
            throw new ServiceNotFoundException(TRACEABILITY_SERVICE_ERROR);
        }
    }


}
