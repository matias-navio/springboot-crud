package com.matias.springboot.app.crudjpa.springbootcrud.services;

import java.util.List;
import java.util.Optional;

import com.matias.springboot.app.crudjpa.springbootcrud.entities.Endpoint;

public interface IEndpointService {

    List<Endpoint> findAll();

    Endpoint create(Endpoint endpoint);

    Optional<Endpoint> delete(Long id);

    boolean existsByPath(String path);
}
