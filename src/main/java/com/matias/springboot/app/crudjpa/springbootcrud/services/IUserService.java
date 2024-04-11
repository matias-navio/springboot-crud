package com.matias.springboot.app.crudjpa.springbootcrud.services;

import java.util.List;

import com.matias.springboot.app.crudjpa.springbootcrud.entities.User;

public interface IUserService {

    public List<User> findAll();
    
    public User create(User user);

}
