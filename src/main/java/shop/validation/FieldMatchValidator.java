package shop.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Objects;
import org.springframework.beans.BeanWrapperImpl;

public class FieldMatchValidator
        implements ConstraintValidator<FieldMatch, String> {
    private String firstFieldName;
    private String secondFieldName;

    @Override
    public void initialize(FieldMatch constraintAnnotation) {
        this.firstFieldName = constraintAnnotation.firstFieldName();
        this.secondFieldName = constraintAnnotation.secondFieldName();
    }

    @Override
    public boolean isValid(String field, ConstraintValidatorContext constraintValidatorContext) {
        String firstFieldValue = (String) new BeanWrapperImpl(field)
                .getPropertyValue(firstFieldName);
        String secondFieldValue = (String) new BeanWrapperImpl(field)
                .getPropertyValue(secondFieldName);

        return Objects.equals(firstFieldValue, secondFieldValue);
    }
}
