package com.astro95.springcloud.backend.ecomerce.productservice.controller;

import com.astro95.springcloud.backend.ecomerce.productservice.model.Product;

import com.astro95.springcloud.backend.ecomerce.productservice.service.IProductoService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import java.util.List;


@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/products")
public class productController {

    @Autowired
    private IProductoService service;

    @GetMapping
    public ResponseEntity<List<Product>> listar (){
        List<Product> products = service.getAllProducts();
        if(products.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok().body(products);
    }

    @PostMapping("/crear")
    public ResponseEntity<Product> crear (@Valid @RequestBody Product product) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(product));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> findById(@PathVariable Long id){
        return ResponseEntity.ok().body(service.getProductById(id));
    }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<Product> update(@Valid @RequestBody Product product,@PathVariable Long id){
        return ResponseEntity.ok().body(service.update(product, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id){
        return ResponseEntity.ok().body(service.DeleteProductById(id));
    }

    @GetMapping("/name")
    public ResponseEntity<List<Product>> findByName(@NotBlank @RequestParam String name){
        List<Product> products = service.findByName(name);
        if (products.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok().body(service.findByName(name));
    }

    @GetMapping("/price")
    public ResponseEntity<List<Product>> findByPriceBetween(@NotNull @RequestParam Double minPrice, @NotNull @RequestParam Double maxPrice){
        List<Product> products = service.findByPriceBetween(minPrice, maxPrice);
        if (products.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(products);
    }

    @GetMapping("/category")
    public ResponseEntity<List<Product>> findByCategory(@NotBlank (message = "La categoria no puede ser nula o vacia ") @RequestParam  String category){
        List<Product> products = service.findByCategory(category);
        if (products.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok().body(products);
    }

    @PatchMapping("/updateStock/{id}")
    public ResponseEntity<Product> upddateStock (@PathVariable Long id, @Positive @RequestParam Integer stock){
        return ResponseEntity.ok().body(service.updateStock(id, stock));
    }

    @GetMapping("/ProductPrice/{productId}")
    public ResponseEntity<Double> getPriceById(@PathVariable Long productId){
        return ResponseEntity.ok().body(service.getProductPriceById(productId));
    }

}
