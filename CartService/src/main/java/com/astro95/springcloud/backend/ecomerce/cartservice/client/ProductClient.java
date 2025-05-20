package com.astro95.springcloud.backend.ecomerce.cartservice.client;

import com.astro95.springcloud.backend.ecomerce.cartservice.models.ProductDto;
import org.bson.types.ObjectId;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "product-service", path = "/products")
public interface ProductClient {

    @GetMapping("/ProductPrice/{productId}")
    Double getProductPrice(@PathVariable Long productId);

    @GetMapping("/{id}")
    ProductDto getProductDetails(@PathVariable Long id);
}
