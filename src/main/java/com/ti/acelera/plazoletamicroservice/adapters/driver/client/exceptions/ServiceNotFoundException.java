package com.ti.acelera.plazoletamicroservice.adapters.driver.client.exceptions;

public class ServiceNotFoundException extends RuntimeException {

    public ServiceNotFoundException() {
        super();
    }

    public ServiceNotFoundException(String message) {
        super(message);
    }

    public ServiceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceNotFoundException(Throwable cause) {
        super(cause);
    }
}
