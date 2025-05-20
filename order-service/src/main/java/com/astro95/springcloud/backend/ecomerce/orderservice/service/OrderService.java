package com.astro95.springcloud.backend.ecomerce.orderservice.service;

import com.astro95.springcloud.backend.ecomerce.orderservice.model.Order;
import org.bson.types.ObjectId;

import java.util.List;

public interface OrderService {

    Order createOrder(Order order, ObjectId userId);
    Order getOrderById(ObjectId id);
    Order updateOrderStatus(ObjectId orderId, String status);
    void deleteOrderById(ObjectId id);
    List<Order> getOrdersByUserId(ObjectId userId);
    List<Order> getAllOrders();

}
