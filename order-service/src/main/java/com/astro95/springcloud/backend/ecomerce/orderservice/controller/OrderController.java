package com.astro95.springcloud.backend.ecomerce.orderservice.controller;

import com.astro95.springcloud.backend.ecomerce.orderservice.Exception.GeneralAppException;
import com.astro95.springcloud.backend.ecomerce.orderservice.model.Order;
import com.astro95.springcloud.backend.ecomerce.orderservice.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@Validated
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    @PostMapping("/{userId}")
    public Order create(@RequestBody Order order,@PathVariable String userId) {
        ObjectId objectId = new ObjectId(userId);
        return orderService.createOrder(order, objectId);
    }

    @GetMapping("/{orderId}")
    public Order getById(@PathVariable ObjectId orderId) {
        return orderService.getOrderById(orderId);
    }

    @PutMapping("/{id}")
    public Order updateStatus(@PathVariable ObjectId id,@RequestParam String status) {
        return orderService.updateOrderStatus(id, status);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable ObjectId id) {
        orderService.deleteOrderById(id);
    }

    @GetMapping("/getByUserId/{userId}")
    public List<Order> getByUserId(@PathVariable ObjectId userId) {
        return orderService.getOrdersByUserId(userId);
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
