package com.matias.springboot.app.crudjpa.springbootcrud.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;

import com.matias.springboot.app.crudjpa.springbootcrud.entities.Product;
import com.matias.springboot.app.crudjpa.springbootcrud.services.ProductServiceImpl;
import com.matias.springboot.app.crudjpa.springbootcrud.validation.ProductValidation;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@RestController
@RequestMapping("/crud")
public class ProductRestController {

    @Autowired
    private ProductServiceImpl productService;

    @Autowired
    private ProductValidation validation;

    @GetMapping("/products")
    public List<Product> list(){
        return productService.findAll();
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<?> findOne(@PathVariable Long id){
        Optional<Product> prodOptional = productService.findById(id);
        if(prodOptional.isPresent()){
            return ResponseEntity.ok(prodOptional.orElseThrow());
        }

        return ResponseEntity.notFound().build();
    }

    // el BindingResult no puede ir al final, tiene que ir despues del objeto
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody Product product, BindingResult result){
        validation.validate(product, result);
        if(result.hasFieldErrors()){
            return validation(result);
        }

        Product newProduct = productService.save(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(newProduct);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@Valid @RequestBody Product product, BindingResult result, @PathVariable Long id){
        validation.validate(product, result);
        if(result.hasFieldErrors()){
            return validation(result);
        }
        
        Optional<Product> optionalProduct = productService.update(id, product);
        if(optionalProduct.isPresent()){
            return ResponseEntity.status(HttpStatus.CREATED).body(optionalProduct.orElseThrow());
        }

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Product> remove(@PathVariable Long id){
        Optional<Product> optionalProd = productService.delete(id);
        if(optionalProd.isPresent()){
            return ResponseEntity.ok(optionalProd.orElseThrow());
        }

        return ResponseEntity.notFound().build();
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
