package com.astro95.springcloud.backend.ecomerce.userservice.exception;

public class GeneralAppException extends RuntimeException {

    private Integer status;

    public GeneralAppException(String message, Integer status) {
        super(message);
        this.status = status;
    }

    public Integer getStatus() {
        return status;
    }


}
