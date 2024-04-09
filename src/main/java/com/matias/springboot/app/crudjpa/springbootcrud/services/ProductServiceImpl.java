package com.matias.springboot.app.crudjpa.springbootcrud.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.matias.springboot.app.crudjpa.springbootcrud.entities.Product;
import com.matias.springboot.app.crudjpa.springbootcrud.repositories.ProductRepository;


@Service
public class ProductServiceImpl implements IProductService{

    @Autowired
    private ProductRepository repository;

    @Transactional(readOnly = true)
    @Override
    public List<Product> findAll() {
        
        return (List<Product>) repository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Product> findById(Long id) {
        
        return repository.findById(id);
    }

    @Transactional
    @Override
    public Product save(Product product) {

        return repository.save(product);
    }

    @Transactional
    @Override
    public Optional<Product> update(Long id, Product product) {
        Optional<Product> optionalProduct = repository.findById(id);
        if(optionalProduct.isPresent()){
            Product prod = optionalProduct.orElseThrow();
            prod.setName(product.getName());
            prod.setPrice(product.getPrice());
            prod.setDescription(product.getDescription());

            return Optional.of(repository.save(prod));
        }
        return optionalProduct;
    }

    @Transactional
    @Override
    public Optional<Product> delete(Long id) {

        Optional<Product> optionalProduct = repository.findById(id);
        optionalProduct.ifPresent(productDb -> {
            repository.delete(productDb);
        });
        // devolvemos el producto para que no muestre un msj en caso de que no se encuentre en la base de datos
        return optionalProduct;
    }

    
}
