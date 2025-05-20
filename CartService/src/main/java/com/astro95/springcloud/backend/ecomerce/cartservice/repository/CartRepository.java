package com.astro95.springcloud.backend.ecomerce.cartservice.repository;

import com.astro95.springcloud.backend.ecomerce.cartservice.models.Cart;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface CartRepository extends MongoRepository<Cart, ObjectId> {

    Optional<Cart> findByUserId(ObjectId userId);

}
