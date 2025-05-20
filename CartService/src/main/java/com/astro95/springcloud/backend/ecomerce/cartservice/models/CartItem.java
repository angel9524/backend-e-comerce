package com.astro95.springcloud.backend.ecomerce.cartservice.models;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItem {


    @NotNull(message = "El id del priducto no puede ser nulo")
    private Long productId;

    @Min(value = 1, message = "La cantidad debe ser almenos 1")
    private Integer quantity;

    @NotNull(message = "El precio unitario no puede ser nulo")
    private Double unitPrice;

}
