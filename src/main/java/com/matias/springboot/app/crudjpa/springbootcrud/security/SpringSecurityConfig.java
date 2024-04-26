package com.matias.springboot.app.crudjpa.springbootcrud.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity 
public class SpringSecurityConfig {

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @SuppressWarnings("deprecation")
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception{

        return http.authorizeRequests((authz) -> authz
                    .requestMatchers(HttpMethod.GET ,"/crud/users/**").permitAll() // permite solo metodos get
                    .requestMatchers(HttpMethod.POST ,"/crud/users/register").hasAnyRole("ROLE_ADMIN") // permite solo metodos post pero el register
                    .anyRequest()
                    .denyAll()
                )
                .csrf(config -> config.disable()) // desabilita csrf
                // determina una sesion sin estado, es decir, no vamos a guardar la sesion en memoria
                .sessionManagement(managmanet -> managmanet.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) 
                .build();


        
    }
}
