package com.astro95.springcloud.backend.ecomerce.productservice.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProductNotFoundException extends RuntimeException {

  private static final Logger logger = LoggerFactory.getLogger(ProductNotFoundException.class);
  public ProductNotFoundException(String message) {
    super(message);
  }
}
