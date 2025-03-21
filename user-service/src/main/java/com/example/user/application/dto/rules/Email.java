package com.example.user.application.dto.rules;

import jakarta.validation.Constraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@NotBlank(message = "Email is required")
@jakarta.validation.constraints.Email(message = "Email should be valid")
@Pattern(
        regexp = "^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$",
        message = "Email format is invalid"
)
@Constraint(validatedBy = {})
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface Email {
    String message() default "Invalid username";
    Class<?>[] groups() default {};
    Class<? extends jakarta.validation.Payload>[] payload() default {};
}
