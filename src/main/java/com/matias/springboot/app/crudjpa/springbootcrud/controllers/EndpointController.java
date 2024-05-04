package com.matias.springboot.app.crudjpa.springbootcrud.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.matias.springboot.app.crudjpa.springbootcrud.entities.Endpoint;
import com.matias.springboot.app.crudjpa.springbootcrud.services.EnpointServiceImpl;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/crud/endpoint")
public class EndpointController {

    @Autowired
    private EnpointServiceImpl enpointService;

    @GetMapping("/list")
    public List<Endpoint> listAll(){
        return enpointService.findAll();
    }

    @PostMapping("/save")
    public ResponseEntity<?> create(@Valid @RequestBody Endpoint endpoint, BindingResult result){
        if(result.hasFieldErrors()){
            return validation(result);
        }

        return ResponseEntity.status(200).body(enpointService.create(endpoint));
    }

    @DeleteMapping("/detele/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        Optional<Endpoint> optionalEndpoint = enpointService.delete(id);
        if(optionalEndpoint.isPresent()){
            return ResponseEntity.ok(optionalEndpoint.orElseThrow());
        }

        return ResponseEntity.badRequest().build();
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
