package com.matias.springboot.app.crudjpa.springbootcrud.services.security;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.matias.springboot.app.crudjpa.springbootcrud.entities.User;
import com.matias.springboot.app.crudjpa.springbootcrud.repositories.UserRepository;

@Service
public class JpaUserDetailsService implements UserDetailsService{

    @Autowired
    private UserRepository repository;

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // verificamos si el user se encuentra en la DB
        User user = repository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException(username + " no está registrado en la base de datos"));
       

        // lista de tipo GrantedAuthority de los roles del user
        /*
         * a partir de la lista de roles del user, creamos un stream
         * y con el metodo map pasamos esa lista de roles a una lista SimpleGrantedAuthority
         */
        List<GrantedAuthority> authorities = user.getRoles().stream()
            .map(role -> new SimpleGrantedAuthority(role.getName()))
            .collect(Collectors.toList());

        // importamos la clase User pero de Spring Security, importamos el paquete porque ya tenemos la clase User nuestra
        return new org.springframework.security.core.userdetails.User(
            user.getUsername(),
            user.getPassword(),
            user.isEnabled(),
            true,
            true,
            true,
            authorities
        );
    }

}
