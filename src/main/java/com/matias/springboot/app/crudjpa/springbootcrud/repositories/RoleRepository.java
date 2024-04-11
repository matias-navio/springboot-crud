package com.matias.springboot.app.crudjpa.springbootcrud.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.matias.springboot.app.crudjpa.springbootcrud.entities.Role;

public interface RoleRepository extends CrudRepository<Role, Long>{

    // es mejor buscar el Role por el name, ya que el ID puede variar
    Optional<Role> findByName(String name);
}
