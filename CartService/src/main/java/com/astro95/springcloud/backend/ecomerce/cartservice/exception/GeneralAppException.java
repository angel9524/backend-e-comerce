package com.astro95.springcloud.backend.ecomerce.cartservice.exception;

public class GeneralAppException extends RuntimeException {


    private final Integer statusCode;
    public GeneralAppException(String message, Integer statusCode) {
        super(message);
        this.statusCode = statusCode;
    }
    public Integer getStatusCode() {
        return statusCode;
    }


}
