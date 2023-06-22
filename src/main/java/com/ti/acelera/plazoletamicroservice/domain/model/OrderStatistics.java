package com.ti.acelera.plazoletamicroservice.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Duration;
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderStatistics {
    private Long orderId;
    private Duration orderExecutionTime;

}