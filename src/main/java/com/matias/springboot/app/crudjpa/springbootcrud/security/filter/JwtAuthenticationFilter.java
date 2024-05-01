package com.matias.springboot.app.crudjpa.springbootcrud.security.filter;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// con esto importamos todo lo que hay dentro de esa clase
import static com.matias.springboot.app.crudjpa.springbootcrud.security.TokenJwtConfig.*;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter{
    
    private AuthenticationManager authenticationManager;

    

    // generamos el constructor con la clase que extendemos
    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    // implementamos este metodo de la clase UsernamePasswordAuthenticationFilter
    /*
     *  recibe username y password del request mediante un POST en formato jSON
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

    // este método se ejecuta en caso de que todo salga bien, y la autenticación haya sido exitosa
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
            Authentication authResult) throws IOException, ServletException {

            /*
             *  Extraemos datos del usuario autenticado en el objeto Authentication
             *  se transforma el principal a un User de security para obtener el username
             * 
             * se importa User de spring security
             */
            User user = (User) authResult.getPrincipal();
            String username = user.getUsername();
            Collection<? extends GrantedAuthority> roles = user.getAuthorities();

            /*
             * Construimos un claim con los roles del user
             * para despues agregarlo al token
             */
            Claims claims = Jwts.claims().build();
            claims.put("roles", roles);

            /*
             *  Esto lo copiamos del repo de JWT, crea el token
             *  a partir del username y de la llave que generamos arriba
             */
            String token = Jwts.builder()
                .subject(username)
                .claims(claims)
                .expiration(new Date(System.currentTimeMillis() + 3600000)) // le agregamos una expiración (fecha actual mas una hora)
                .issuedAt(new Date()) // le agregamos la fecha actual
                .signWith(SECRET_KEY)
                .compact();

            /*
             *  Encabezado de la autorización en la respuesta
             *  Las constantes vienen de la clase TokenJwtConfig
             *  
             *  Es la manera estandar de enviar tokens con Bearer seguido del token
             */
            response.addHeader(HEADER_AUTHORIZATION, PREFIX_TOKEN + token);

            /*
             *  Se crea un Map para almacenar los datos que se le van a enviar al cliente
             */
            Map<String, String> body = new HashMap<>();
            body.put("token", token);
            body.put("username", username);
            body.put("message", "Hola " + username + " has iniciado sesión exitosamente!");

            /*  
                serealizacion del mapa a formato JSON usando ObjectMapper
                se define el contenido como formato JSON
                se define el status como 200 OK
            */
            response.getWriter().write(new ObjectMapper().writeValueAsString(body));
            response.setContentType(CONTENT_TYPE);
            response.setStatus(200);
        }

        /*
         * Método en caso de que la autenticación haya fallado
         * crea un mapa con los msjs de error
         */
        @Override
        protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                AuthenticationException failed) throws IOException, ServletException {
            
            Map<String, String> body = new HashMap<>();
            body.put("message", "Error en la autenticación, username o password incorrectos!");
            body.put("error", failed.getMessage());

            response.getWriter().write(new ObjectMapper().writeValueAsString(body));
            response.setContentType(CONTENT_TYPE);
            response.setStatus(401);
        }
}
