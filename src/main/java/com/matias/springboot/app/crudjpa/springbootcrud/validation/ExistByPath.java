package com.matias.springboot.app.crudjpa.springbootcrud.validation;

import jakarta.validation.Payload;

public @interface ExistByPath {

    String message() default "la ruta ya existe en la base de datos";

	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };

}
