package com.astro95.springcloud.backend.ecomerce.productservice.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GeneralAppException extends RuntimeException {

    private static final Logger logger = LoggerFactory.getLogger(GeneralAppException.class);

    private final Integer statusCode;
    public GeneralAppException(String message, Integer statusCode) {
        super(message);
        this.statusCode = statusCode;

    }
    public int getStatusCode() {
        return statusCode;
    }

}
