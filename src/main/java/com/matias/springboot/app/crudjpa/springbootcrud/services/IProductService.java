package com.matias.springboot.app.crudjpa.springbootcrud.services;

import java.util.List;
import java.util.Optional;

import com.matias.springboot.app.crudjpa.springbootcrud.entities.Product;

public interface IProductService {

    List<Product> findAll();

    Optional<Product> findById(Long id);

    Product save(Product product);

    Optional<Product> update(Long id, Product product);

    Optional<Product> delete(Long id);
}
