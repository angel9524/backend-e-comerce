package com.astro95.springcloud.backend.ecomerce.productservice.repository;

import com.astro95.springcloud.backend.ecomerce.productservice.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRespository extends JpaRepository<Product, Long> {
    List<Product> findByCategory(String category);
    List<Product> findByNameContainingIgnoreCase(String name);
    List<Product> findByPriceBetween(Double lower, Double higher);
}
