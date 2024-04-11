package com.matias.springboot.app.crudjpa.springbootcrud.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.matias.springboot.app.crudjpa.springbootcrud.entities.Role;
import com.matias.springboot.app.crudjpa.springbootcrud.entities.User;
import com.matias.springboot.app.crudjpa.springbootcrud.repositories.RoleRepository;
import com.matias.springboot.app.crudjpa.springbootcrud.repositories.UserRepository;

@Service
public class UserServiceImpl implements IUserService{

    @Autowired
    private UserRepository repository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    @Override
    public List<User> findAll() {
        return (List<User>) repository.findAll();
    }

    @Transactional
    @Override
    public User create(User user) {

        Optional<Role> optionalRoleUser = roleRepository.findByName("ROLE_USER");
        List<Role> roles = new ArrayList<>();
        
        // si esta presente el ROLE_USER lo agregamos a la lista roles 
        optionalRoleUser.ifPresent(roles::add);

        if(user.isAdmin()){
            Optional<Role> optionalRoleAdmin = roleRepository.findByName("ROLE_ADMIN");
            // si el ROLE_ADMIN está presente lo agregamos a la lista
            optionalRoleAdmin.ifPresent(roles::add);
        }

        user.setRoles(roles);
        // le pasamos el password que viene del request y lo codificamos
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return repository.save(user);
    }

}
