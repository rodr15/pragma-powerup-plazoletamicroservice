package com.ti.acelera.plazoletamicroservice.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RestaurantState {
    ACTIVE("ACTIVE", 1),
    DELETED("DELETED", 2),
    PENDING_DELETE("PENDING DELETE", 3);

    private final String state;
    private final int order;



}
