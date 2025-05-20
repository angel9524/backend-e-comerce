package com.astro95.springcloud.backend.ecomerce.cartservice.controller;

import com.astro95.springcloud.backend.ecomerce.cartservice.exception.GeneralAppException;
import com.astro95.springcloud.backend.ecomerce.cartservice.models.Cart;
import com.astro95.springcloud.backend.ecomerce.cartservice.models.ProductDto;
import com.astro95.springcloud.backend.ecomerce.cartservice.services.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;

    @PostMapping("/get-or-create/{id}")
    public ResponseEntity<Cart> getOrCreateCart(@Valid @PathVariable String id) {
        ObjectId objectId = getObjectId(id);
        return ResponseEntity.ok(cartService.getOrCreateCart(objectId));
    }

    @PutMapping("/add-item-car/{userId}/{productId}/{quantity}")
    public ResponseEntity<Object> addItemToCart(@Valid @PathVariable String userId,@Valid @PathVariable Long productId,@Valid @PathVariable Integer quantity) {
        ObjectId objectId = getObjectId(userId);
        cartService.addItemToCart(objectId, productId, quantity);
        return ResponseEntity.ok().build();
    }
    @DeleteMapping("/delete-item-from-cart/{userId}/{productId}")
    public ResponseEntity<Object> removeItemFromCart(@PathVariable String userId,@PathVariable Long productId){
        ObjectId objectId = getObjectId(userId);
        cartService.removeItemFromCart(objectId, productId);
        return ResponseEntity.ok().build();
    }
    @PutMapping("update-cart-item-quantiry/{userId}/{productId}/{quantity}")
    public ResponseEntity<Cart> updateCartItemQuantity(@Valid @PathVariable String userId,@Valid @PathVariable Long productId,@Valid @PathVariable Integer quantity) {
        ObjectId objectId = getObjectId(userId);
        return ResponseEntity.ok().body(cartService.updateCartItemQuantiry(objectId, productId, quantity));
    }

    @GetMapping("/get-product-details/{productId}")
    public ResponseEntity<ProductDto> getProductDetails(@Valid @PathVariable Long productId) {
        return ResponseEntity.ok(cartService.getProductDetails(productId));
    }

    private static ObjectId getObjectId(String id) {
        ObjectId objectId;
        try {
            objectId = new ObjectId(id);
        }catch (IllegalArgumentException e) {
            throw new GeneralAppException("ID invalido", 400);
        }
        return objectId;
    }



}
