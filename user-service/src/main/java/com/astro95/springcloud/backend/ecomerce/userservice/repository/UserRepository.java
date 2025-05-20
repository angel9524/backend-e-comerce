package com.astro95.springcloud.backend.ecomerce.userservice.repository;

import com.astro95.springcloud.backend.ecomerce.userservice.model.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, ObjectId> {
    Optional<User> findByUsername(String username);
}
