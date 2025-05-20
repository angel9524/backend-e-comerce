//package com.astro95.springcloud.backend.ecomerce.getaway.service;
//
//import com.astro95.springcloud.backend.ecomerce.getaway.clients.UserClient;
//import com.astro95.springcloud.backend.ecomerce.getaway.models.LoginRequest;
//import com.astro95.springcloud.backend.ecomerce.getaway.models.UserRespone;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor
//public class AuthService implements AuthServiceI{
//
//
//    private final UserClient userClient;
//    private final JwtService jwtService;
//
//    public String login(LoginRequest loginRequest) {
//        try {
//            UserRespone login = userClient.login(loginRequest).getBody();
//            if (login == null) {
//                throw new Exception();
//            }
//            return jwtService.generateToken(login.getUsername(), login.getRoles());
//
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//
//    }
//}
