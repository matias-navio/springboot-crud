package com.matias.springboot.app.crudjpa.springbootcrud.security;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.matias.springboot.app.crudjpa.springbootcrud.entities.Endpoint;
import com.matias.springboot.app.crudjpa.springbootcrud.security.filter.JwtAuthenticationFilter;
import com.matias.springboot.app.crudjpa.springbootcrud.security.filter.JwtValidationFilter;
import com.matias.springboot.app.crudjpa.springbootcrud.services.EnpointServiceImpl;

@Configuration
@EnableWebSecurity 
@EnableMethodSecurity(prePostEnabled = true)
public class SpringSecurityConfig {

    @Autowired
    private AuthenticationConfiguration configuration;

    @Autowired
    private EnpointServiceImpl enpointService;

    @Bean
    AuthenticationManager authenticationManager() throws Exception{
        return configuration.getAuthenticationManager();
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @SuppressWarnings("deprecation")
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception{

        List<Endpoint> publicEndpoints = enpointService.findAll().stream()
            .filter(endpoint -> endpoint.getRoles().stream()
            .anyMatch(role -> role.getName().equals("USER")))
            .collect(Collectors.toList());

        List<Endpoint> privateEndpoints = enpointService.findAll().stream()
            .filter(endpoint -> endpoint.getRoles().stream()
            .anyMatch(role -> role.getName().equals("ADMIN")))
            .collect(Collectors.toList());        

        http.authorizeRequests((auth) -> 
            auth.requestMatchers(HttpMethod.GET, "/crud/users/list").permitAll()
                .requestMatchers(HttpMethod.GET, "/crud/products/list").permitAll()
                .requestMatchers(HttpMethod.GET, "/crud/products/{id}").permitAll()
                .anyRequest().authenticated())

                // agregamos filtro de autenticación
                .addFilter(new JwtAuthenticationFilter(authenticationManager()))
                // agregamos filtro de validación
                .addFilter(new JwtValidationFilter(authenticationManager()))
                .csrf(config -> config.disable()) // desabilita csrf
                // determina una sesion sin estado, es decir, no vamos a guardar la sesion en memoria
                .sessionManagement(managmanet -> managmanet.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
}

