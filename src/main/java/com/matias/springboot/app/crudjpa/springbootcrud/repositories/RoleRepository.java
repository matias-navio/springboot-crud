package com.matias.springboot.app.crudjpa.springbootcrud.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.matias.springboot.app.crudjpa.springbootcrud.entities.Role;

public interface RoleRepository extends CrudRepository<Role, Long>{

    public Optional<Role> findByName(String name);
}
