package com.matias.springboot.app.crudjpa.springbootcrud.services;

import java.util.List;
import java.util.Optional;

import com.matias.springboot.app.crudjpa.springbootcrud.entities.User;

public interface IUserService {

    List<User> findAll();

    User create(User user);

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);
}
