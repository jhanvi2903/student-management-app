package com.example.student_management_app.customannotation;


import com.example.student_management_app.validator.PasswordMatchesValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PasswordMatchesValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface PasswordMatches {

    String message()
            default "Password and repeat password do not match";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
