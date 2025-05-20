package com.astro95.springcloud.backend.ecomerce.getaway.models;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class UserRespone {
    private String username;
    private String password;
    private List<String> roles = new ArrayList<>();
}
