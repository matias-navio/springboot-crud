package com.matias.springboot.app.crudjpa.springbootcrud.services;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.matias.springboot.app.crudjpa.springbootcrud.entities.Endpoint;
import com.matias.springboot.app.crudjpa.springbootcrud.entities.Role;
import com.matias.springboot.app.crudjpa.springbootcrud.repositories.EndpointRepository;
import com.matias.springboot.app.crudjpa.springbootcrud.repositories.RoleRepository;

@Service
public class EnpointServiceImpl implements IEndpointService{

    @Autowired
    private EndpointRepository enpointRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Transactional(readOnly = true)
    @Override
    public List<Endpoint> findAll() {
        return (List<Endpoint>) enpointRepository.findAll();
    }

    @Transactional
    @Override
    public Endpoint create(Endpoint endpoint) {

        Optional<Role> optionalRoleAdmin = roleRepository.findByName("ROLE_ADMIN");
        Set<Role> roles = new HashSet<>();

        optionalRoleAdmin.ifPresent(roles::add);

        if(endpoint.isRoleUser()){
            Optional<Role> optionalRoleUser = roleRepository.findByName("ROLE_USER");
            optionalRoleUser.ifPresent(roles::add);
        }

        endpoint.setRoles(roles);

        return enpointRepository.save(endpoint);
    }

    @Transactional
    @Override
    public Optional<Endpoint> delete(Long id) {

        Optional<Endpoint> optionalEndpoint = enpointRepository.findById(id);
        optionalEndpoint.ifPresent(endpoint -> enpointRepository.delete(endpoint));
        return optionalEndpoint;
    }

    @Override
    public boolean existsByPath(String path) {
        return enpointRepository.existsByPath(path);
    }

}
