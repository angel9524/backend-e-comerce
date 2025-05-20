//package com.astro95.springcloud.backend.ecomerce.getaway.controller;
//
//
//import com.astro95.springcloud.backend.ecomerce.getaway.models.LoginRequest;
//
//import com.astro95.springcloud.backend.ecomerce.getaway.service.AuthServiceI;
//import lombok.RequiredArgsConstructor;
//
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/auth")
//public class AuthController {
//
//    private final AuthServiceI authService;
//
//    @PostMapping("/login")
//    public String login(@RequestBody LoginRequest loginRequest) {
//        return authService.login(loginRequest);
//
//    }
//
//}
