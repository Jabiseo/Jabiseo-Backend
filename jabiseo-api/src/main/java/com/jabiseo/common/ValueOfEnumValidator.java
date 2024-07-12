package com.jabiseo.common;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValueOfEnumValidator implements ConstraintValidator<EnumValid, String> {

    private EnumValid enumValid;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        boolean result = false;
        if(value == null){
            return false;
        }

        Enum<?>[] enumValues = this.enumValid.enumClass().getEnumConstants();
        if (enumValues != null) {
            for (Object enumValue : enumValues) {
                if (value.equals(enumValue.toString())
                        || this.enumValid.ignoreCase() && value.equalsIgnoreCase(enumValue.toString())) {
                    result = true;
                    break;
                }
            }
        }
        return result;
    }

    @Override
    public void initialize(EnumValid constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        this.enumValid = constraintAnnotation;
    }
}
