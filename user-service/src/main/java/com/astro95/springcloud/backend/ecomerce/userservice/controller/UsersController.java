package com.astro95.springcloud.backend.ecomerce.userservice.controller;

import com.astro95.springcloud.backend.ecomerce.userservice.model.Role;
import com.astro95.springcloud.backend.ecomerce.userservice.model.User;
import com.astro95.springcloud.backend.ecomerce.userservice.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Validated
@RequestMapping("/users")
@RequiredArgsConstructor
public class UsersController {

    Logger logger = LoggerFactory.getLogger(UsersController.class);

    private final UserService userService;

    @GetMapping
    public List<User> getAll(){
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getById(@NotBlank @PathVariable String id){
        ObjectId objectId = new ObjectId(id);
        return ResponseEntity.ok().body(userService.getUserById(objectId));
    }

    @PostMapping("/{id}/{role}")
    public ResponseEntity<String> asignRole (@NotBlank @PathVariable String id,@NotBlank @PathVariable Role role){
        ObjectId objectId = new ObjectId(id);
        userService.asignRole(objectId,role);
        return ResponseEntity.ok().body("El rol se ha asignado correctamente");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delteById(@NotBlank @PathVariable String id){
        ObjectId objectId = new ObjectId(id);
        userService.deleteUserById(objectId);
        return ResponseEntity.ok().body("El usuarios se ha eliminado correctamente");
    }

    @PutMapping("/update")
    public ResponseEntity<User> update(@Valid @RequestBody User user){
        return ResponseEntity.ok().body(userService.updateUser(user));
    }

}