package com.ti.acelera.plazoletamicroservice.domain.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class Traceability {
    private Long orderId;
    private Long employeeId;
    private Long clientId;
    private LocalDateTime updatedAt;
    private LocalDateTime createdAt;
    private String lastState;
    private String currentState;

    @Override
    public String toString() {
        return "Trace{" +
                ", orderId=" + orderId +
                ", employeeId=" + employeeId +
                ", clientId=" + clientId +
                ", updatedAt=" + updatedAt +
                ", lastState='" + lastState +
                ", currentState='" + currentState +
                '}';
    }
}

