package com.astro95.springcloud.backend.ecomerce.cartservice.models;

import com.mongodb.event.ConnectionCheckedOutEvent;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "carts")
public class Cart {


    @Id
    private ObjectId id;

    @NotNull(message = "El Id del usuario no puede ser nulo")
    private ObjectId userId;

    @NotEmpty(message = "El carrito debe contener almenos un producto")
    private List<CartItem> items = new ArrayList<>();

    private Double total;

    public void asignarCalcularTotal(){
        this.total = calcularTotal();
    }

    public Double calcularTotal(){
        return items.stream()
                .mapToDouble(item -> item.getUnitPrice() * item.getQuantity())
                .sum();
    }

    public Boolean isEmpty(){
        return items.isEmpty() || items == null;
    }

    public int getTotalQuiantity(){
        return items.stream().mapToInt(item -> item.getQuantity()).sum();
    }
}
