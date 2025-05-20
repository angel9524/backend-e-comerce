package com.astro95.springcloud.backend.ecomerce.productservice.service;

import com.astro95.springcloud.backend.ecomerce.productservice.model.Product;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface IProductoService {

    List<Product> getAllProducts();
    Product getProductById(Long id);
    Product save(Product product);
    Product update(Product product, Long id);
    String DeleteProductById(Long id);
    List<Product> findByName(String name);
    List<Product> findByPriceBetween(double lower, double upper);
    List<Product> findByCategory(String category);
    Product updateStock(Long id, Integer stock);
    Double getProductPriceById(Long productId);


}
