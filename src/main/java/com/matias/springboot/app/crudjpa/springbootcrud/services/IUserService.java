package com.matias.springboot.app.crudjpa.springbootcrud.services;

import java.util.List;

import com.matias.springboot.app.crudjpa.springbootcrud.entities.User;

public interface IUserService {

    List<User> findAll();

    User create(User user);
}
