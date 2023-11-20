package by.sapra.restclientservice.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = OrderFilterValidValidator.class)
@Target(TYPE)
@Retention(value = RUNTIME)
public @interface OrderFilterValid {
    String message() default "Поля пагинации должны быть указаны! Если вы указываете minCost или maxCost то оба должны быть указаны!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
