package com.matias.springboot.app.crudjpa.springbootcrud.validation;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.matias.springboot.app.crudjpa.springbootcrud.entities.Product;

@Component
public class ProductValidation implements Validator{

    private String MESSAGE_REQUIRED = "es requerido!";   
    private String MESSAGE_NULL = "no puede ser nulo!";   
    
    // con este método asignamos la clase que queremos validar
    @Override
    public boolean supports(Class<?> clazz) {
        return Product.class.isAssignableFrom(clazz);
    }

    // el tipo Object hace referencia al objeto Product en este caso y Errors a BindingResult
    @Override
    public void validate(Object target, Errors errors) {
        Product product = (Product) target;

        // ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "NotBlank.product.isRequired");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "description", null, MESSAGE_REQUIRED);
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "price", null, MESSAGE_NULL);

        if(product.getName() == null || product.getName().isBlank()){
            errors.rejectValue("name", null,  MESSAGE_REQUIRED);
        }else if(product.getName().length() < 3 || product.getName().length() > 15){
            errors.rejectValue("name", null,  "debe tener entre 3 y 15 caracteres");
        }

    }

}
