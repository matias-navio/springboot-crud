package com.matias.springboot.app.crudjpa.springbootcrud.security;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

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
                // agregamos los cors a Spring Security
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                // determina una sesion sin estado, es decir, no vamos a guardar la sesion en memoria
                .sessionManagement(managmanet -> managmanet.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration config = new CorsConfiguration();

        // establece todos los patrones de origenes permitos para los CORS
        config.setAllowedOriginPatterns(Arrays.asList("*"));
        // establece los métodos HTTP que permite en el origen
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
        // establece las cabeceras HTTP que se pueden enviar en las solicitudes 
        config.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        // establece credenciales verdaderas(coockies, tokens) establecidas en el navegador
        config.setAllowCredentials(true);

        /* 
            permite definir las configuraciones de arriba,
            para distintas rutas, en este caso la raiz /**,
            luego retorna la confi con la ruta
        */
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }

    @Bean
    FilterRegistrationBean<CorsFilter> corsFilter(){
        // se crea una instancia filter Register
        FilterRegistrationBean<CorsFilter> corsBean = new FilterRegistrationBean<>(new CorsFilter(corsConfigurationSource()));

        // se establece una prioridad alta
        corsBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return corsBean;
    }
}

