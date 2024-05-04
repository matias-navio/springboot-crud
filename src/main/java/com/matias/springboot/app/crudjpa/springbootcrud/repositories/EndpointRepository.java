package com.matias.springboot.app.crudjpa.springbootcrud.repositories;

import org.springframework.data.repository.CrudRepository;

import com.matias.springboot.app.crudjpa.springbootcrud.entities.Endpoint;

public interface EndpointRepository extends CrudRepository<Endpoint, Long>{

    boolean existsByPath(String path);
}
