package com.astro95.springcloud.backend.ecomerce.orderservice.client;

import com.astro95.springcloud.backend.ecomerce.orderservice.model.ProductDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "product-service", path = "/products")
public interface ProductClient {

    @GetMapping("/{id}")
    ProductDto getProductDetails(@PathVariable Long id);
}
