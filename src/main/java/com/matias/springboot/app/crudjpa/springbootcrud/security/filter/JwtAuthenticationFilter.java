package com.matias.springboot.app.crudjpa.springbootcrud.security.filter;

import java.io.IOException;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.matias.springboot.app.crudjpa.springbootcrud.entities.User;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter{
    
    private AuthenticationManager authenticationManager;

    // generamos el constructor con la clase que extendemos
    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    // implementamos este metodo de la clase UsernamePasswordAuthenticationFilter
    /*
     *  recibe username y password del reques mediante un POST en formato jSON
     *  este método se encarga de convertir el JSON a un objeto de Java
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        // objetos vacios de user, password y username que necesitamos para el token
        User user = null;
        String username = null;
        String password = null;
        // para convertir el json a objeto java
        ObjectMapper data = new ObjectMapper();

        try {
            /*
                con este método a partir del reques que viene en formato JSON 
                lo podemos convertir el User
            */
            user = data.readValue(request.getInputStream(), User.class);
            username = user.getUsername();
            password = user.getPassword();

        } catch (StreamReadException e) {
            e.printStackTrace();
        } catch (DatabindException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // creamos el token con el username y password que viene del request y esta guardado el DB, para autenticarlo
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);

        // se retorna un authenticate
        return authenticationManager.authenticate(token);
    }

    
}
