package com.matias.springboot.app.crudjpa.springbootcrud.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.matias.springboot.app.crudjpa.springbootcrud.entities.User;
import com.matias.springboot.app.crudjpa.springbootcrud.services.UserServiceImpl;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/crud/users")
public class UserController {

    @Autowired
    private UserServiceImpl userService;

    @GetMapping("/list")
    public List<User> listUser(){
        
        return userService.findAll();
    }

    @PostMapping("/save")
    public ResponseEntity<?> save(@Valid @RequestBody User user, BindingResult result){
        if(result.hasFieldErrors()){
            return validation(result);
        }

        return ResponseEntity.status(201).body(userService.create(user));
    }

    private ResponseEntity<?> validation(BindingResult result) {
        // creamos un mapa para almacenar los errores 
        Map<String, Object> errors = new HashMap<>(); 
        result.getFieldErrors().forEach(err -> {
            errors.put(err.getField(), "El campo " + err.getField() + " " + err.getDefaultMessage());
        });
        // los devolvemos con badRequest, que sería un 400
        return ResponseEntity.badRequest().body(errors);
    }
}
