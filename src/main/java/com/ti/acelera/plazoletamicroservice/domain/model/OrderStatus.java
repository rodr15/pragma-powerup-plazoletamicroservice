package com.ti.acelera.plazoletamicroservice.domain.model;

public enum OrderStatus {
    EARRING_ORDER("EARRING", 1),
    IN_PREPARATION_ORDER("IN PREPARATION", 2),
    READY_ORDER("READY", 3),
    FINISHED_ORDER("FINISHED", 4);

    private final String status;
    private final int order;

    OrderStatus(String status, int order) {
        this.status = status;
        this.order = order;
    }

    public String getStatus() {
        return status;
    }

    public int getOrder() {
        return order;
    }

    public OrderStatus next() {
        int nextOrder = this.order + 1;
        for (OrderStatus orderStatus : OrderStatus.values()) {
            if (orderStatus.order == nextOrder) {
                return orderStatus;
            }
        }
        return OrderStatus.FINISHED_ORDER;
    }
}
