package com.astro95.springcloud.backend.ecomerce.cartservice.services;

import com.astro95.springcloud.backend.ecomerce.cartservice.DTO.UserDto;
import com.astro95.springcloud.backend.ecomerce.cartservice.client.ProductClient;
import com.astro95.springcloud.backend.ecomerce.cartservice.client.UserClient;
import com.astro95.springcloud.backend.ecomerce.cartservice.exception.GeneralAppException;
import com.astro95.springcloud.backend.ecomerce.cartservice.models.Cart;
import com.astro95.springcloud.backend.ecomerce.cartservice.models.CartItem;
import com.astro95.springcloud.backend.ecomerce.cartservice.models.ProductDto;
import com.astro95.springcloud.backend.ecomerce.cartservice.repository.CartRepository;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService implements ICartService {

    private final CartRepository cartRepository;

    private final Logger logger = LoggerFactory.getLogger(CartService.class);

    private final ProductClient productClient;

    private final UserClient userClient;

    @Override
    public Cart getOrCreateCart(ObjectId userId) {
        try {
            Optional<Cart> op = cartRepository.findByUserId(userId);

            if (!op.isPresent()) {
                logger.info("Creando carrito");
                Cart cart = new Cart();
                cart.setUserId(userId);
                return cartRepository.save(cart);
            }
            op.get().asignarCalcularTotal();
            return op.get();
        } catch (DataAccessException e){
            logger.error("Error al acceder a la base de datos {} " , e.getMessage());
            throw new GeneralAppException("Error al acceder a la base de datos " + e.getMessage(), 500);
        } catch (Exception e) {
            logger.error("Error inesperado al buscar el carrito {} " , e.getMessage());
            throw new GeneralAppException("Error inseperado al buscar  " +  e.getMessage(), 500);
        }
    }
    @Override
    public void addItemToCart(ObjectId userId, Long productId, Integer quantity) {
        try {
            existsUser(userId);
            existsProduct(productId);
            if (quantity <= 0) {
                logger.warn("La cantidad debe ser mayor que 0");
                throw new GeneralAppException("La cantidad debe ser mayor que 0", 400);
            }

            Double unitPrice = productClient.getProductPrice(productId);
            Cart cart = getOrCreateCart(userId);
            if (cart.getItems().stream().anyMatch(item -> item.getProductId().equals(productId))){
                logger.warn("No se puede agregar el producto con el id {} por que ya esta en el carrito", productId );
                throw new GeneralAppException("El producto ya esta en el carrito", 400);
            }
            cart.getItems().add(new CartItem(productId, quantity, unitPrice));
            cartRepository.save(cart);
        } catch (DataAccessException e){
            logger.error("Error al acceder a la base de datos {} " , e.getMessage());
            throw new GeneralAppException("Error al acceder a la base de datos " + e.getMessage(), 500);
        } catch (Exception e){
            logger.error("Error inseperado {} " , e.getMessage());
            throw new GeneralAppException("Error inseperado " +  e.getMessage(), 500);
        }
    }

    @Override
    public void removeItemFromCart(ObjectId userId, Long productId) {
        try {

            existsUser(userId);
            existsProduct(productId);

            Cart cart = getOrCreateCart(userId);
            if (!cart.getItems().stream().anyMatch(item -> item.getProductId().equals(productId))){
                logger.warn("El producto con el id {} no existe", productId);
                throw new GeneralAppException("El producto no existe", 404);
            }
            cart.getItems().removeIf(product -> product.getProductId().equals(productId));
            cartRepository.save(cart);
            logger.info("El producto con el id {} se elimino del carrito", productId);
        } catch (DataAccessException e){
            logger.error("Error al acceder a la base de datos {} " , e.getMessage());
            throw new GeneralAppException("Error al acceder a la base de datos " + e.getMessage(), 500);
        }catch (Exception e){
            logger.error("Error inseperado {} " , e.getMessage());
            throw new GeneralAppException("Error inseperado " +  e.getMessage(), 500);
        }
    }

    @Override
    public Cart updateCartItemQuantiry(ObjectId userID, Long productId, Integer quantity) {
        try {

            existsUser(userID);
            existsProduct(productId);

            if (quantity <= 0) {
                logger.warn("La cantidad debe ser mayor que 0");
                throw new GeneralAppException("La cantidad debe ser mayor que 0", 400);
            }


            Cart cart = getOrCreateCart(userID);
            if (!cart.getItems().stream().anyMatch(item -> item.getProductId().equals(productId))){
                logger.warn("El producto con el id {} no existe en el carrito", productId);
                throw new GeneralAppException("El producto no existe", 404);
            }
            cart.getItems().stream().filter(product -> product.getProductId().equals(productId))
                            .forEach(product -> product.setQuantity(quantity));

            logger.info("La cantidad de productos se actualizo con exito");
            return cartRepository.save(cart);
        } catch (DataAccessException e){
            logger.error("Error al acceder a la base de datos {} " , e.getMessage());
            throw new GeneralAppException(e.getMessage(), 500);
        } catch (Exception e){
            logger.error("Error inseperado {} " , e.getMessage());
            throw new GeneralAppException("Error inseperado " +  e.getMessage(), 500);
        }
    }
    @Override
    public ProductDto getProductDetails(Long productId) {
        try {
            return productClient.getProductDetails(productId);
        }catch (FeignException.NotFound e){
            logger.error("No se encontre el producto con el Id {}", productId);
            throw new GeneralAppException("No se encontre el producto ", 404);
        }catch (FeignException e) {
                logger.error("Error al comunicarse con el ProductService para obtener el producto con ID {}: {}",
                productId, e.getMessage());
                throw new GeneralAppException("Error al comunicarse con el ProductService: " + e.getMessage(), 500);
        } catch (Exception e) {
        logger.error("Error inesperado al obtener los detalles del producto con ID {}: {}",
                productId, e.getMessage());
        throw new GeneralAppException("Error inesperado: " + e.getMessage(), 500);
    }


    }
    private void existsUser(ObjectId userId) {
        try {
           userClient.getUserById(userId);

        } catch (FeignException.NotFound e) {
            throw new GeneralAppException(e.getMessage(), 404);
        }catch (FeignException e){
            throw new GeneralAppException(e.getMessage(), 500);
        }
    }

    private void existsProduct(Long Id) {
        try {
            productClient.getProductDetails(Id);
        } catch (FeignException.NotFound e) {
            throw new GeneralAppException(e.getMessage(), 404);
        }catch (FeignException e){
            throw new GeneralAppException(e.getMessage(), 500);
        }
    }
}
