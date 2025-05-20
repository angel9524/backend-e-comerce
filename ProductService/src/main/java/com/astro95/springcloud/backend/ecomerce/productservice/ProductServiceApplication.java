package com.astro95.springcloud.backend.ecomerce.productservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
public class ProductServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(ProductServiceApplication.class, args);
	}

}
