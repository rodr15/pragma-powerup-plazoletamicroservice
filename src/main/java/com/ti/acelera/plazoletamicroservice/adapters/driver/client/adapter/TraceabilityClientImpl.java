package com.ti.acelera.plazoletamicroservice.adapters.driver.client.adapter;

import com.nimbusds.jose.shaded.gson.JsonObject;
import com.ti.acelera.plazoletamicroservice.adapters.driver.client.exceptions.ServiceNotFoundException;
import com.ti.acelera.plazoletamicroservice.domain.gateway.ITraceabilityClient;
import com.ti.acelera.plazoletamicroservice.domain.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

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
            String url = String.format("%s/trace", traceabilityServiceUrl);

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
            String url = String.format("%s/trace", traceabilityServiceUrl);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            JsonObject orderJsonObject = new JsonObject();

            orderJsonObject.addProperty("orderId", order.getId());
            orderJsonObject.addProperty("updatedAt", LocalDateTime.now().toString());
            orderJsonObject.addProperty("currentState", order.getOrderStatus().toString());
            orderJsonObject.addProperty("employeeId", order.getIdChef());

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

            String url = String.format("%s/trace?orderId=%s", traceabilityServiceUrl, orderId);
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

            HttpEntity<?> entity = new HttpEntity<>(headers);

            ResponseEntity<List<Traceability>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<List<Traceability>>() {
                    });
            return response.getBody();

        } catch (Exception e) {
            throw new ServiceNotFoundException(TRACEABILITY_SERVICE_ERROR);
        }
    }

    @Override
    public List<EmployeeStatistics> getEmployeeStatistics(List<Long> employeesId) {
        RestTemplate restTemplate = new RestTemplate();

        try {
            UriComponentsBuilder builder = UriComponentsBuilder.newInstance();

            for (Long employeeId : employeesId) {
                builder.queryParam("employeesId", employeeId);
            }
            String paramsString = builder.build().getQuery();

            String url = String.format("%s/statistics/employee?%s", traceabilityServiceUrl,paramsString);

            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

            HttpEntity<?> entity = new HttpEntity<>(headers);

            ResponseEntity<List<EmployeeStatistics>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<List<EmployeeStatistics>>() {}
                    );


            return response.getBody();


        } catch (Exception e) {
            throw new ServiceNotFoundException(TRACEABILITY_SERVICE_ERROR);
        }

    }

    @Override
    public List<OrderStatistics> getOrdersStatistics(List<Long> ordersId) {
        RestTemplate restTemplate = new RestTemplate();

        try {
            UriComponentsBuilder builder = UriComponentsBuilder.newInstance();

            for (Long employeeId : ordersId) {
                builder.queryParam("ordersId", employeeId);
            }
            String paramsString = builder.build().getQuery();

            String url = String.format("%s/statistics/orders?%s", traceabilityServiceUrl,paramsString);

            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

            HttpEntity<?> entity = new HttpEntity<>(headers);

            ResponseEntity<List<OrderStatistics>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<List<OrderStatistics>>() {}
            );

            return response.getBody();


        } catch (Exception e) {
            throw new ServiceNotFoundException(TRACEABILITY_SERVICE_ERROR);
        }
    }

    @Override
    public void saveRestaurantTrace(RestaurantObjectsTrace restaurantObjectsTrace) {
        RestTemplate restTemplate = new RestTemplate();

        try {
            String url = String.format("%s/restaurant-trace", traceabilityServiceUrl);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            JsonObject orderJsonObject = new JsonObject();
            orderJsonObject.addProperty("restaurantId", restaurantObjectsTrace.getRestaurantId());
            orderJsonObject.addProperty("state", restaurantObjectsTrace.getState());
            orderJsonObject.addProperty("objectId", restaurantObjectsTrace.getObjectId());
            orderJsonObject.addProperty("objectType", restaurantObjectsTrace.getObjectType());

            HttpEntity<String> request =
                    new HttpEntity<>(orderJsonObject.toString(), headers);

            restTemplate.postForObject(url, request, String.class);

        } catch (Exception e) {
            throw new ServiceNotFoundException(TRACEABILITY_SERVICE_ERROR);
        }
    }


}
