package com.astro95.springcloud.backend.ecomerce.productservice.service;

import com.astro95.springcloud.backend.ecomerce.productservice.exception.GeneralAppException;
import com.astro95.springcloud.backend.ecomerce.productservice.exception.ProductNotFoundException;
import com.astro95.springcloud.backend.ecomerce.productservice.model.Product;
import com.astro95.springcloud.backend.ecomerce.productservice.repository.ProductRespository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Validated
public class ProductService implements IProductoService {

    private final ProductRespository repository;

    private final Logger logger = LoggerFactory.getLogger(ProductService.class);

    @Override
    @Transactional(readOnly = true)
    public List<Product> getAllProducts() {
        try {
            List<Product> products = repository.findAll();
            if (products.isEmpty()) {
                logger.warn("No products found");
            } else {
                logger.info("Found {} products", products.size());
            }
            return products;
        } catch (DataAccessException e) {
            logger.error("Error el consultar productos", e.getMessage(), e);
            throw new GeneralAppException("Error al intentar obtener los productos", 500);
        } catch (Exception e) {
            logger.error("Error inseperado al obtener los productos");
            throw new GeneralAppException("Error inesperado al intentar obtener los productos", 500);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Product getProductById(Long id) {
        try {
            Optional<Product> op = repository.findById(id);
            if (!op.isPresent()) {
                logger.warn("el producto con el id " + id + " no existe");
                throw new ProductNotFoundException("el producto con el id " + id + " no existe" + id);
            }
            return op.get();
        } catch (DataAccessException e) {
            logger.error("Error en la base de datos  ", e.getMessage(), e);
            throw new GeneralAppException("Error en la base de datos  ", 500);
        } catch (Exception e) {
            logger.error("Error inseperado al buscar el producto con el id " + id + " ");
            throw new GeneralAppException("Error inseperado al buscar el producto con el id " + id + " ", 500);
        }
    }

    @Override
    @Transactional
    public Product save(Product product) {
        try {
            return repository.save(product);
        } catch (DataIntegrityViolationException e) {
            logger.error("Error el intentar guardar en la base de datos" + e);
            throw new GeneralAppException("Error al intentar guardar en la base de datos", 500);
        } catch (Exception e) {
            logger.error("Error inesperado al guardar en la base de datos" + e);
            throw new GeneralAppException("Error inesperado al intentar guardar en la base de datos", 500);
        }
    }

    @Override
    @Transactional
    public Product update(Product product, Long id) {
        try {
            if (id == null || id <= 0) {
                logger.warn("El id del producto es null o 0");
                throw new IllegalArgumentException("El id del producto es null o 0");
            }
            Optional<Product> op = repository.findById(id);
            if (!op.isPresent()) {
                logger.warn("el producto con el id " + id + " no existe");
                throw new ProductNotFoundException("el producto con el id " + id + " no existe");
            }
            Product productDb = op.get();
            productDb.setName(product.getName());
            productDb.setDescription(product.getDescription());
            productDb.setPrice(product.getPrice());
            productDb.setQuantity(product.getQuantity());
            productDb.setCategory(product.getCategory());
            return repository.save(productDb);
        } catch (DataIntegrityViolationException e) {
            logger.error("Error el intentar guardar en la base de datos" + e, e.getMessage());
            throw new GeneralAppException("Error al intentar guardar en la base de datos", 500);
        } catch (Exception e) {
            logger.error("Error inseperado al intentar actualizar los datos" + e.getMessage() + e);
            throw new GeneralAppException("Error inesperado al intentar actualizar los datos", 500);
        }
    }

    @Override
    @Transactional
    public String DeleteProductById(Long id) {
        try {

            Optional<Product> op = repository.findById(id);
            if (!op.isPresent()) {
                logger.warn("el producto con el id " + id + " no existe");
                throw new ProductNotFoundException("el producto con el id " + id + " no existe");
            }
            repository.deleteById(id);
            return "Producto eliminado correctamente";
        } catch (EmptyResultDataAccessException e) {
            logger.warn("Producto no encontrado" + id);
            throw new ProductNotFoundException("Producto not found" + id);

        } catch (DataIntegrityViolationException e) {
            logger.error("Error al intentar eliminar el producto " + id);
            throw new GeneralAppException("Error al intentar eliminar el producto", 500);
        } catch (Exception e) {
            logger.error("Error al intentar eliminar el producto " + id, e);
            throw new GeneralAppException("Error inesperado al intentar eliminar el producto", 500);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> findByName(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("El nombre del producto es requerido");
        }
        try {
            List<Product> products = repository.findByNameContainingIgnoreCase(name);
            if (products.isEmpty()) {
                logger.warn("No se encontraron prodcutos con el nombre " + name);
                return Collections.emptyList();
            }
            logger.info("se encontraron productos con el nombre " + name);
            return products;
        } catch (DataAccessException e) {
            logger.error("Error al consultar productos con el nombre " + name);
            throw new GeneralAppException("Error al consultar productos con el nombre " + name, 500);
        } catch (Exception e) {
            logger.error("Error inesperado al consultar productos con el nombre " + name, e);
            throw new GeneralAppException("Error inseperado al buscar productos", 500);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> findByPriceBetween(double lower, double upper) {
        if (lower < 0 || upper < 0) {
            throw new IllegalArgumentException("Los valores de precio deben ser positivos");
        }
        if (lower > upper) {
            throw new IllegalArgumentException("El valor mínimo debe ser menor o igual al valor máximo");
        }
        try {
            List<Product> products = repository.findByPriceBetween(lower, upper);
            if (products.isEmpty()) {
                logger.warn("No se encontraron productos con precios entre {} y {}", lower, upper);
                return Collections.emptyList();
            }
            logger.info("Se encontraron {} productos con precios entre {} y {}", products.size(), lower, upper);
            return products;
        } catch (DataAccessException e) {
            logger.error("Error al consultar productos con precios entre {} y {}: {}", lower, upper, e.getMessage());
            throw new GeneralAppException("Error al intentar buscar productos por rango de precios", 500);
        } catch (Exception e) {
            logger.error("Error inesperado al buscar productos con precios entre {} y {}: {}", lower, upper, e.getMessage(), e);
            throw new GeneralAppException("Error inesperado al buscar productos por rango de precios", 500);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> findByCategory(String category) {
        try {
            List<Product> products = repository.findByCategory(category);
            if (products.isEmpty()) {
                logger.warn("No se encontraron productos con categoria {}", category);
                return List.of();
            }
            logger.info("Se encontraron productos con categoria {}", category);
            return products;
        } catch (DataAccessException e) {
            logger.error("Error al consultar en la base de datos productos con categoria {}", category);
            throw new GeneralAppException("Error en la base de datos al buscar por categoria", 500);
        } catch (Exception e) {
            logger.error("Error inseperado al buscar los productos por categoria {}", category, e);
            throw new GeneralAppException("Error inseperado al inentar buscar por categoria", 500);
        }
    }

    public Product updateStock(Long id, Integer stock){
        try {
            Optional<Product> op = repository.findById(id);
            if (op.isEmpty()){
                logger.warn("El producto con el id {} no existe", id);
                throw new GeneralAppException("El producto no existe", 404);
            }
            Product product = op.get();
            product.setQuantity(stock);
            logger.info("Se actualizo el stock correctamente");
            return product;
        } catch (DataAccessException e) {
            logger.error("Error en la base de datos al actualizar el producto {} " , id, e);
            throw new GeneralAppException("Error en la base de datos", 500);
        } catch (Exception e) {
            logger.error("Error inesperado al actualizar el producto {} " , id, e);
            throw new GeneralAppException("Error inesperado al actualizar el producto", 500);
        }
    }

    @Override
    public Double getProductPriceById(Long productId) {
        try {
            return getProductById(productId).getPrice();
        } catch (Exception e) {
            logger.error("Error inesperado al obtener el producto {} ", productId, e);
            throw new GeneralAppException("Error en la base de datos", 500);
        }
    }
}
