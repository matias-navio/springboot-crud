package com.matias.springboot.app.crudjpa.springbootcrud.validation;

import org.springframework.beans.factory.annotation.Autowired;

import com.matias.springboot.app.crudjpa.springbootcrud.services.EnpointServiceImpl;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ExistByPathValidation implements ConstraintValidator<ExistByPath, String>{

    @Autowired
    private EnpointServiceImpl enpointService;

    @Override
    public boolean isValid(String path, ConstraintValidatorContext context) {
        
        if(enpointService == null){
            return true;
        }

        return !enpointService.existsByPath(path);
    }

}
