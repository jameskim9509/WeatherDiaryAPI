package zerobase.weather.exception;

import lombok.Getter;
import zerobase.weather.type.ErrorCode;

@Getter
public class WeatherDiaryException extends RuntimeException{
    private final ErrorCode errorCode;

    public WeatherDiaryException(ErrorCode errorCode)
    {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
