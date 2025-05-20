package com.astro95.springcloud.backend.ecomerce.getaway.models;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
}
