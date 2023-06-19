package com.ti.acelera.plazoletamicroservice.domain.exceptions;

public class OrderStatusNotAllowedForThisActionException  extends RuntimeException {
    public OrderStatusNotAllowedForThisActionException() {super();}
    public OrderStatusNotAllowedForThisActionException(String message) {
        super(message);
    }
}

