package com.ti.acelera.plazoletamicroservice.adapters.driver.client.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TraceabilityDto {
    private Long orderId;
    private Long employeeId;
    private Long clientId;
    private LocalDateTime updatedAt;
    private LocalDateTime createdAt;
    private String lastState;
    private String currentState;
}
