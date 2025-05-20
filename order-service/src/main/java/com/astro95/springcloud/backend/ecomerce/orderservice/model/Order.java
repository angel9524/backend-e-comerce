package com.astro95.springcloud.backend.ecomerce.orderservice.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
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
@Document(collection = "orders")
@AllArgsConstructor
@NoArgsConstructor
public class Order {

    @Id
    @JsonSerialize(using = ObjectIdJsonSerializer.class)
    private ObjectId id;
    @NotNull
    @JsonSerialize(using = ObjectIdJsonSerializer.class)
    private ObjectId userId;
    @NotNull
    private List<orderItem> items = new ArrayList<>();
    @NotNull
    private Double totalAmount;
    @NotNull
    private String status;

}
