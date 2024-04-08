package com.matias.springboot.app.crudjpa.springbootcrud.repositories;

import org.springframework.data.repository.CrudRepository;

import com.matias.springboot.app.crudjpa.springbootcrud.entities.Product;

public interface ProductRepository extends CrudRepository<Product, Long>{

}
