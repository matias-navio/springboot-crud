package com.matias.springboot.app.crudjpa.springbootcrud.security.filter;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.matias.springboot.app.crudjpa.springbootcrud.security.SimpleGrantedAuthorityJsonCreator;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import static com.matias.springboot.app.crudjpa.springbootcrud.security.TokenJwtConfig.*;

public class JwtValidationFilter extends BasicAuthenticationFilter{

    // constructor que le pase el authenticationManager a la clase padre
    public JwtValidationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        String header = request.getHeader(HEADER_AUTHORIZATION);

        /*
         *  Si el header es nulo o no comienza con Bearer
         *  nos salimos ya que no es correcto continuar
         */
        if(header == null || !header.startsWith(PREFIX_TOKEN)){
            return;
        }

        /* 
            obtenemos solo el token sin prefijos ni nada
            esto se hace con el método replace, reeemplaza el Bearer por algo vacío
        */
        String token = header.replace(PREFIX_TOKEN, "");

        /*
         *  Creamos los claims del token
         *  y manejamos un error en caso de que no existe
         */
        try {
            Claims claims = Jwts
                .parser()
                .verifyWith(SECRET_KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload();

            // dos maneras distintas de obtener el username
            String username = claims.getSubject();
            // tenemos que llamarlo de la misma manera que lo nombramos en el filter
            // String username2 = (String) claims.get("username");

            Object authoritiesClaims = claims.get("ahutorities"); // obtenemos los roles de la misma manera que arriba

            /*
             *  Convertimos los roles que obtenemos en formato JSON
             *  en este caso a una lista de SimpleGrantedAuthority
             */
            Collection<? extends GrantedAuthority> authorities = java.util.Arrays.asList(
                new ObjectMapper()
                // con esto facilitamos la deserealización de authoritiesClaims como objeto JSON
                .addMixIn(SimpleGrantedAuthority.class, SimpleGrantedAuthorityJsonCreator.class)
                .readValue(authoritiesClaims.toString().getBytes(), SimpleGrantedAuthority[].class)
            );

            /*
             *  Esto permite autenticar un user con su username
             *  y con los permisos que tiene a los recursos de nuestra aplicación
             *  entonces con eso podemos verificar que permisos tiene
             */
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, null, authorities);
            // con esto establecemos el user autenticado al contexto de seguridad actual
            SecurityContextHolder.getContext().setAuthentication(authToken);
            chain.doFilter(request, response);

        } catch (JwtException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            error.put("message", "El token JWT no es válido");

            response.getWriter().write(new ObjectMapper().writeValueAsString(error));
            response.setContentType(CONTENT_TYPE);
            response.setStatus(401);
        }
    }
}
