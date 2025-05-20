package com.astro95.springcloud.backend.ecomerce.orderservice.service;

import com.astro95.springcloud.backend.ecomerce.orderservice.Exception.GeneralAppException;
import com.astro95.springcloud.backend.ecomerce.orderservice.client.ProductClient;
import com.astro95.springcloud.backend.ecomerce.orderservice.client.UserClient;
import com.astro95.springcloud.backend.ecomerce.orderservice.model.Order;
import com.astro95.springcloud.backend.ecomerce.orderservice.model.ProductDto;
import com.astro95.springcloud.backend.ecomerce.orderservice.model.UserDto;
import com.astro95.springcloud.backend.ecomerce.orderservice.model.orderItem;
import com.astro95.springcloud.backend.ecomerce.orderservice.repository.OrderRepository;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    private final OrderRepository orderRepository;

    private final ProductClient productClient;

    private final UserClient userClient;


    @Override
    public Order createOrder(Order order, ObjectId userId) {
        try {

            if (order == null || order.getItems() == null || order.getItems().isEmpty()) {
                throw new GeneralAppException("La orden no puede estar vacia", 400);
            }

            UserDto user;
            try {
                user = userClient.getUserDetails(userId);
                if (user == null) {
                    logger.error("El usuario con el id {} no existe", userId);
                    throw new GeneralAppException("El usuario con el id " + userId + " no existe", 404);
                }
            }catch (FeignException.NotFound e){
                logger.error("El usuario con el id {} no existe", userId);
                throw new GeneralAppException(e.getMessage(), 400);
            }catch (FeignException e){
                logger.error("Error al comunicarse con el servicio usuarios");
                throw new GeneralAppException(e.getMessage(), 500);
            }
            double total = 0;
            for (orderItem item: order.getItems()){
                ProductDto product;
                try {
                    product = productClient.getProductDetails(item.getProductId());
                    if (product == null) {
                        throw new GeneralAppException("El producto con el ID " + item.getProductId() + " no existe", 400);
                    }
                    if (product.getPrice() == null){
                        throw new GeneralAppException("El producto no esta disponible", 400);
                    }
                    total += product.getPrice() * item.getQuantity();
                }catch (FeignException.NotFound e){
                    throw new GeneralAppException(e.getMessage(), 400);
                }catch (FeignException e){
                    throw new GeneralAppException(e.getMessage(), 500);
                }
            }
            Order orderDb = new Order();
            orderDb.setUserId(userId);
            orderDb.setItems(order.getItems());
            orderDb.setTotalAmount(total);
            orderDb.setStatus("PENDING");
            logger.info("Orden creada con exito");
            return orderRepository.save(orderDb);
        } catch (DataAccessException e) {
            logger.error("Eror con la base de datos al guardar la orden {} ", e.getMessage());
            throw new GeneralAppException("Error con la base de datos al guardar la orden " + e, 500);
        } catch (Exception e) {
            logger.error("Error inesperado al guardar la orden {} ", e.getMessage());
            throw new GeneralAppException("Error inesperado al guardar la orden " + e, 500);
        }
    }


    @Override
    public Order getOrderById(ObjectId id) {
        try {
            Order order = orderRepository.findById(id).orElseThrow(() -> new GeneralAppException("La orden con el id" + id +  " no existe", 404));
            logger.info("Orden con el id {} encontrada con exito" , id);
            return order;
        } catch (DataAccessException e) {
            logger.error("Error al consultar en la base de datos la orden {} ", e.getMessage());
            throw new GeneralAppException("Error al consultar la orden  en la base de datos" + e, 500);
        } catch (Exception e) {
            logger.error("Error inesperado al consultar la orden {} ", e.getMessage());
            throw new GeneralAppException("Error inseperado al consultar la orden " + e, 500);
        }
    }

    @Override
    public List<Order> getAllOrders(){
        try {
            return orderRepository.findAll();
        }catch (DataAccessException e){
            logger.error("Error al consultar las ordenes en la base de datos" + e.getMessage());
            throw new GeneralAppException("Error en la base de datos al consultar las ordenes", 500);
        }catch (Exception e){
            logger.error("Error inesperado al consultar las ordenes " + e.getMessage());
            throw new GeneralAppException("Error inesperado al consultar las ordenes " + e, 500);
        }
    }


    @Override
    public Order updateOrderStatus(ObjectId orderId, String status) {
        try {
            Order order = orderRepository.findById(orderId)
                    .orElseThrow(() -> new GeneralAppException("La orden no existe", 400));
            order.setStatus(status);
            logger.info("Estado de la orden {} actualizado a {}", orderId, status);
            return orderRepository.save(order);

        } catch (DataAccessException e) {
            logger.error("Error en la base de datos al actualizar el status de la orden {} ", e.getMessage());
            throw new GeneralAppException("Error en la base de datos al actualizar el status de la orden " + e, 500);
        } catch (Exception e) {
            logger.error("Error inesperado al actualizar la orden {} ", e.getMessage());
            throw new GeneralAppException("Error inesperado al actualizar la orden " + e, 500);
        }
    }

    @Override
    public void deleteOrderById(ObjectId id) {
        try {
            if (!orderRepository.existsById(id)){
                logger.error("La orden no existe");
                throw new GeneralAppException("La orden no existe", 404);
            }
            logger.info("Orden con el id {} eliminada con exito", id);
            orderRepository.deleteById(id);
        } catch (DataAccessException e) {
            logger.error("Error en la base de datos del orden {} ", e.getMessage());
            throw new GeneralAppException("Error en la base de datos del orden " + e, 500);
        } catch (Exception e) {
            logger.error("Error inesperado al eliminar la orden {} ", e.getMessage());
            throw new GeneralAppException("Error inesperado al eliminar la orden " + e, 500);
        }
    }

    @Override
    public List<Order> getOrdersByUserId(ObjectId userId) {
        try {

            UserDto userDto;
            try {
                userDto = userClient.getUserDetails(userId);
                if (userDto == null) {
                    throw new GeneralAppException("El usuario con el id " + userId + " no existe", 400);
                }
            }catch (FeignException.NotFound e){
                logger.error("Error al comunicarse con el servicio Usuarios");
                throw new GeneralAppException("Error al comunicarse con el servicio Usuarios", 500);
            }catch (FeignException e){
                logger.error("Error al comunicarse con el servicio Usuarios");
                throw new GeneralAppException("Error al comunicarse con el servicio Usuarios", 500);
            }
            List<Order> orders = orderRepository.findAllByUserId(userId);
            if (orders == null || orders.isEmpty()) {
                logger.error("No existen ordenes con el id {}", userId);
                return Collections.emptyList();
            }
            return orders;
        }catch (DataAccessException e) {
            logger.error("Error en la base de datos del orden {} ", e.getMessage());
            throw new GeneralAppException("Error en la base de datos del orden " + e, 500);
        } catch (Exception e) {
            logger.error("Error inesperado al buscar órdenes para el usuario con ID {}: {}", userId, e.getMessage());
            throw new GeneralAppException("Error inesperado al buscar órdenes", 500);
        }
    }
}
