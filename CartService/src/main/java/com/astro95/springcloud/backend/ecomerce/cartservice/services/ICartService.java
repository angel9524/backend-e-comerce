package com.astro95.springcloud.backend.ecomerce.cartservice.services;

import com.astro95.springcloud.backend.ecomerce.cartservice.models.Cart;
import com.astro95.springcloud.backend.ecomerce.cartservice.models.ProductDto;
import org.bson.types.ObjectId;

public interface ICartService {

    Cart getOrCreateCart(ObjectId userId);
    void addItemToCart(ObjectId userId, Long productId, Integer quantity);
    void removeItemFromCart(ObjectId userId, Long productId);
    Cart updateCartItemQuantiry(ObjectId userID, Long productId, Integer quantity);
    ProductDto getProductDetails(Long productId);
}
