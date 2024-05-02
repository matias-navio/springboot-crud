package com.matias.springboot.app.crudjpa.springbootcrud.security;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/*  
    esta clase sirve para poder facilitar el proceso
    de deserealización de un objeto JSON 

    lo usamos para transformar un JSON a una lista de SimpleGrantedAuthority
    en la clase JwtValidatorFilter
*/
public abstract class SimpleGrantedAuthorityJsonCreator {

    @JsonCreator
    public SimpleGrantedAuthorityJsonCreator(@JsonProperty("authority") String role){}

}
