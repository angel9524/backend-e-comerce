package com.astro95.springcloud.backend.ecomerce.userservice.service;

import com.astro95.springcloud.backend.ecomerce.userservice.exception.GeneralAppException;
import com.astro95.springcloud.backend.ecomerce.userservice.model.Role;
import com.astro95.springcloud.backend.ecomerce.userservice.model.User;
import com.astro95.springcloud.backend.ecomerce.userservice.repository.UserRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.astro95.springcloud.backend.ecomerce.userservice.model.User.isValidRole;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {


    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public String register(User user){
        try {
            if (userRepository.findByUsername(user.getUsername()).isPresent()) {
                logger.warn("El nombre de usuario {} ya existe", user.getUsername());
                throw new GeneralAppException("El nombre de usuario ya esta registrado", 400);
            }
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            user.addRole(Role.USER);
            userRepository.save(user);
            return ("usuario correctamente");
        } catch (DataAccessException e) {
            logger.error("Error con la base de datos {} ", e.getMessage());
            throw new GeneralAppException("Error con la base de datos " + e.getMessage(), 400);
        }catch (Exception e){
            logger.error("Error inseperado al intentar registrar el usuario {} ", e.getMessage());
            throw new GeneralAppException("Error al intentar registrar el usuario " + e.getMessage(), 400);
        }
    }
    @Override
    public User login(User loginRequest){
        User user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new GeneralAppException("El nombre de usuario no existe", 404));
        if (!bCryptPasswordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new GeneralAppException("Error en las credenciales", 500);
        }
        return user;
    }


    @Override
    public User getUserById(ObjectId id) {
        try {
            Optional<User> userOp = userRepository.findById(id);
            if (!userOp.isPresent()) {
                logger.warn("El usuario con el id {} no existe", id);
                throw new GeneralAppException("El usuarios con el id " + id + " no existe", 404);
            }
            logger.info("Se encontro el usuario con el id {}", id);
            return userOp.get();
        }catch (DataAccessException e){
            logger.error("Error con la base de datos {} ", e.getMessage());
            throw new GeneralAppException("Error con la base de datos " + e.getMessage(), 500);
        }catch (Exception e){
            logger.error("Error inesperado al tratar de buscar por id {} ", id);
            throw new GeneralAppException("Error inesperado al tratar de buscar por id " + id, 500);
        }
    }

    @Override
    public User updateUser(User user) {
        try {
            Optional<User> userOp = Optional.ofNullable(getUserById(user.getId()));

            User userDb = userOp.get();
            userDb.setUsername(user.getUsername());
            userDb.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            userDb.setRoles(user.getRoles());
            return userRepository.save(userDb);
        }catch (DataAccessException e){
            logger.error("Error con la base de datos {} ", e.getMessage());
            throw new GeneralAppException("Error con la base de datos " + e.getMessage(), 500);
        }catch (Exception e){
            logger.error("Error inesperado al intentar de actualizar el usuario con el id {} ", user.getId());
            throw new GeneralAppException("Error inseperado al intentar registrar el usuario " + user.getId(), 500);
        }
    }

    @Override
    public void deleteUserById(ObjectId id) {
        try {
            Optional<User> userOp = Optional.ofNullable(getUserById(id));
            userRepository.deleteById(id);
        }catch (DataAccessException e){
            logger.error("Error con la base de datos {} ", e.getMessage());
            throw new GeneralAppException("Error con la base de datos " + e.getMessage(), 500);
        }catch (Exception e){
            logger.error("Error inesperado al tratar de eliminar el usuario con el id {}", id);
            throw new GeneralAppException("Error inesperado al tratar de eliminar el usuario con el id " + id, 500);
        }
    }
    @Override
    public List<User> getAllUsers(){
        try {
            List<User> users = userRepository.findAll();
            if (users.isEmpty()) {
                logger.warn("No se encontraron usuarios");
                return Collections.emptyList();
            }
            return users;
        }catch (DataAccessException e){
            logger.error("Error con la base de datos {} ", e.getMessage());
            throw new GeneralAppException("Error con la base de datos " + e.getMessage(), 500);
        }catch (Exception e){
            logger.error("Error inesperado al intentar de buscar los usuarios");
            throw new GeneralAppException("Error inseperado al intentar de buscar los usuarioss" , 500);
        }
    }

    @Override
    public void asignRole(ObjectId id, Role role) {
        try {
            User user =  getUserById(id);
            if (isValidRole(role)){
                logger.error("El rol proporcionado no es valido", 400);
                throw new GeneralAppException("El rol proporcionado no es valido", 400);
            }

            if (user.getRoles().contains(role)){
                logger.error("El usuario {} ya contiene el rol {}", user.getUsername(), role, 400);
                throw new GeneralAppException("El usuario " + user.getUsername() + " ya contiene el rol " + role, 400);
            }

            user.addRole(role);
            logger.info("El rol se ha asignado correctamente");
            userRepository.save(user);
        }catch (DataAccessException e){
            logger.error("Error con la base de datos {} ", e.getMessage());
            throw new GeneralAppException("Error con la base de datos " + e.getMessage(), 500);
        }catch (Exception e){
            logger.error("Error inesperado al intentar asignar el rol" );
            throw new GeneralAppException("Error inesperado al intentar asignar el rol" +  e.getMessage(), 500);
        }
    }



}
