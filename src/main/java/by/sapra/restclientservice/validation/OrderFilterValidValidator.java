package by.sapra.restclientservice.validation;

import by.sapra.restclientservice.web.model.OrderFilter;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.ObjectUtils;

public class OrderFilterValidValidator implements ConstraintValidator<OrderFilterValid, OrderFilter> {
    @Override
    public boolean isValid(OrderFilter value, ConstraintValidatorContext context) {
        if (ObjectUtils.allNull(value.getMinCost(), value.getMaxCost())) return false;

        return (value.getMinCost() != null || value.getMaxCost() == null)
                && (value.getMinCost() == null || value.getMaxCost() != null);
    }
}
