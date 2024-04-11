package com.matias.springboot.app.crudjpa.springbootcrud.repositories;

import org.springframework.data.repository.CrudRepository;

import com.matias.springboot.app.crudjpa.springbootcrud.entities.User;

public interface UserRepository extends CrudRepository<User, Long>{

}
