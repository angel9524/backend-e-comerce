package com.astro95.springcloud.backend.ecomerce.userservice.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
@Document(collection = "users")
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @JsonSerialize(using = ObjectIdJsonSerializer.class)
    private ObjectId id;

    @NotEmpty(message = "EL nombre de usuario no puede estar vacio")
    private String username;

    @NotEmpty(message = "La contraseña no puede ser vacia")
    private String password;


    private List<Role> roles = new ArrayList<>();

    public void addRole(Role role) {
        this.roles.add(role);
    }

    public static Boolean isValidRole(Role role) {
        List<Role> validRoles = Arrays.asList(Role.values());
        return validRoles.contains(role);
    }

}
