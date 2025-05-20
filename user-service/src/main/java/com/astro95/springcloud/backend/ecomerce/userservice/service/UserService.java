package com.astro95.springcloud.backend.ecomerce.userservice.service;

import com.astro95.springcloud.backend.ecomerce.userservice.model.Role;
import com.astro95.springcloud.backend.ecomerce.userservice.model.User;
import org.bson.types.ObjectId;

import java.util.List;

public interface UserService {

    String register(User user);

    User login(User user);

    User getUserById(ObjectId id);

    User updateUser(User user);

    void deleteUserById(ObjectId id);

    List<User> getAllUsers();

    void asignRole(ObjectId id, Role role);

}
