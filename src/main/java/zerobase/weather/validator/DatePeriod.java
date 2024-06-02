package zerobase.weather.validator;

import javax.validation.Constraint;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DatePeriodValidator.class)
public @interface DatePeriod {
    String message() default "{custom.message}";
    Class[] groups() default {};
    Class[] payload() default {};
}
