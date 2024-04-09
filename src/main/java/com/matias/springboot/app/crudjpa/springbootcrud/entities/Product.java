package com.matias.springboot.app.crudjpa.springbootcrud.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // no puede ser nulo y ademas entre 3 y 15 caracteres
    @NotBlank(message = "{NotBlank.product.name}")
    @Size(min = 3, max = 15, message = "{Size.product.name}")
    private String name;

    // no puede ser nulo
    @NotNull(message = "{NotNull.product.price}")
    private Integer price;

    // no puede ser vacio
    @NotBlank(message = "{NotBlank.product.description}")
    private String description;

    
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Integer getPrice() {
        return price;
    }
    public void setPrice(Integer price) {
        this.price = price;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    

}
