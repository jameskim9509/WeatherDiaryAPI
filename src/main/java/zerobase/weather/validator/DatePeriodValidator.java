package zerobase.weather.validator;

import lombok.extern.slf4j.Slf4j;
import zerobase.weather.type.ErrorCode;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

@Slf4j
public class DatePeriodValidator implements ConstraintValidator<DatePeriod, LocalDate> {
    @Override
    public boolean isValid(LocalDate date, ConstraintValidatorContext context) {
        if(date.isAfter(LocalDate.now())) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    "날짜는 " + LocalDate.now() + "이전이어야 합니다."
            )
            .addConstraintViolation();
            log.error("{}", ErrorCode.INVALID_DATE);
            return false;
        }
        else if(date.isBefore(LocalDate.of(2020,1,1)))
        {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    "날짜는 " + LocalDate.of(2020,1,1) + "이후이어야 합니다."
            )
            .addConstraintViolation();
            log.error("{}", ErrorCode.INVALID_DATE);
            return false;
        }
        return true;
    }
}
