package com.astro95.springcloud.backend.ecomerce.orderservice.client;

import com.astro95.springcloud.backend.ecomerce.orderservice.model.UserDto;
import org.bson.types.ObjectId;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service", path = "/users")
public interface UserClient {

    @GetMapping("/{userId}")
    UserDto getUserDetails(@PathVariable ObjectId userId);
}
