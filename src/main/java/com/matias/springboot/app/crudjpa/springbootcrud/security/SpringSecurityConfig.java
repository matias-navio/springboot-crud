package com.matias.springboot.app.crudjpa.springbootcrud.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.matias.springboot.app.crudjpa.springbootcrud.security.filter.JwtAuthenticationFilter;
import com.matias.springboot.app.crudjpa.springbootcrud.security.filter.JwtValidationFilter;
import com.matias.springboot.app.crudjpa.springbootcrud.services.EnpointServiceImpl;

@Configuration
@EnableWebSecurity 
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

        return http.authorizeRequests((authz) -> authz
                    .requestMatchers(HttpMethod.GET, "/crud/users/**")
                        .permitAll()
                    .requestMatchers(HttpMethod.POST, "/crud/users/register")
                        .permitAll()
                    .requestMatchers(HttpMethod.POST, "/crud/users/save")
                        .hasRole("ADMIN")
                    .requestMatchers(HttpMethod.POST, "/crud/products/save")
                        .hasRole("ADMIN")
                    .requestMatchers(HttpMethod.GET, "/crud/products/list", "/crud/products/{id}")
                        .hasAnyRole("ADMIN", "USER")
                    .requestMatchers(HttpMethod.PUT, "/crud/products/update/{id}")
                        .hasRole("ADMIN")
                    .requestMatchers(HttpMethod.DELETE, "/crud/products/delete/{id}")
                        .hasRole("ADMIN")
                    .requestMatchers(HttpMethod.POST, "/crud/endpoint/save")
                        .hasAnyRole("ADMIN")
                    .requestMatchers(HttpMethod.GET, "/crud/endpoint/list")
                        .hasAnyRole("ADMIN")
                    .anyRequest().authenticated())

                    // agregamos filtro de autenticación
                    .addFilter(new JwtAuthenticationFilter(authenticationManager()))
                    // agregamos filtro de validación
                    .addFilter(new JwtValidationFilter(authenticationManager()))
                    .csrf(config -> config.disable()) // desabilita csrf
                    // determina una sesion sin estado, es decir, no vamos a guardar la sesion en memoria
                    .sessionManagement(managmanet -> managmanet.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) 
                    .build();   
    }
}

