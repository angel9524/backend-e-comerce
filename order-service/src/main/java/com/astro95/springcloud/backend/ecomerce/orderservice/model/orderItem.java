package com.astro95.springcloud.backend.ecomerce.orderservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class orderItem {

    private Long productId;
    private Integer quantity;
    private Double price;

}
