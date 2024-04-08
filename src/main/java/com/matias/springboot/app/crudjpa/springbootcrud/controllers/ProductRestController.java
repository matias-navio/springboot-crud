package com.matias.springboot.app.crudjpa.springbootcrud.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.matias.springboot.app.crudjpa.springbootcrud.entities.Product;
import com.matias.springboot.app.crudjpa.springbootcrud.services.ProductServiceImpl;

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

    @PostMapping
    public ResponseEntity<Product> create(@RequestBody Product product){
        Product newProduct = productService.save(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(newProduct);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Product> update(@PathVariable Long id, @RequestBody Product product){
        product.setId(id);
        Product newProduct = productService.save(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(newProduct);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Product> remove(@PathVariable Long id){
        Product product = new Product();
        product.setId(id);
        Optional<Product> optionalProd = productService.delete(product);

        if(optionalProd.isPresent()){
            return ResponseEntity.ok(optionalProd.orElseThrow());
        }

        return ResponseEntity.notFound().build();
    }
    
}
