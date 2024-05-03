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
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public List<User> findAll() {
        return (List<User>) userRepository.findAll();
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    @Transactional
    public User create(User user) {
        // buscamos ROLE_USER
        Optional<Role> optionalRoleUser = roleRepository.findByName("ROLE_USER");
        List<Role> roles = new ArrayList<>();
        // si existe lo agregamos a la lista
        optionalRoleUser.ifPresent(roles::add);
        // verificamos si el user es admin
        if(user.isAdmin()){
            // si lo es, buscamos el ROLE_ADMIN y lo agregamos a la lista 
            Optional<Role> optionalRoleAdmin = roleRepository.findByName("ROLE_ADMIN");
            optionalRoleAdmin.ifPresent(roles::add);
        }

        user.setRoles(roles);
        // obtenemos el password que viene del request y lo encriptamos
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

}
