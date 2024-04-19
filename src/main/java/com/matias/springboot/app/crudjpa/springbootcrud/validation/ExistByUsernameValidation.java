package com.matias.springboot.app.crudjpa.springbootcrud.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.matias.springboot.app.crudjpa.springbootcrud.services.UserServiceImpl;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

@Component
public class ExistByUsernameValidation implements ConstraintValidator<ExistByUsername, String>{

    @Autowired
    private UserServiceImpl userService;

    @Override
    public boolean isValid(String username, ConstraintValidatorContext context) {
        if(userService == null){
            return true;
        }
        return !userService.existsByUsername(username);
    }

}
