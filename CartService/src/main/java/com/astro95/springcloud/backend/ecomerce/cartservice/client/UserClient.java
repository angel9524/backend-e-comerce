package com.astro95.springcloud.backend.ecomerce.cartservice.client;

import com.astro95.springcloud.backend.ecomerce.cartservice.DTO.UserDto;
import org.bson.types.ObjectId;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service")
public interface UserClient {

    @GetMapping("/users/{id}")
    UserDto getUserById(@PathVariable ObjectId id);
}
