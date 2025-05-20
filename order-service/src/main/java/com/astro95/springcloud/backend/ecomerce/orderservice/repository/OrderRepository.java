package com.astro95.springcloud.backend.ecomerce.orderservice.repository;

import com.astro95.springcloud.backend.ecomerce.orderservice.model.Order;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface OrderRepository extends MongoRepository<Order, ObjectId> {
    List<Order> findAllByUserId(ObjectId userId);

}
