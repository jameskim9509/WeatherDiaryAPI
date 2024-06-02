package zerobase.weather.exception;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import zerobase.weather.dto.ErrorResponse;
import zerobase.weather.type.ErrorCode;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice(basePackages = "zerobase.weather.controller")
public class WeatherDiaryExceptionHandler {

    @ApiResponse(responseCode = "500", description = "내부 오류",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(example = "error")))
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({Exception.class})
    public String ExceptionHandler(Exception e)
    {
        return "error";
    }

    @ApiResponse(responseCode = "400", description = "잘못된 요청",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class)))
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(WeatherDiaryException.class)
    public ErrorResponse handleWeatherDiaryException(WeatherDiaryException e)
    {
        return ErrorResponse.builder()
                .code(e.getErrorCode())
                .message(e.getMessage())
                .build();
    }

    @ApiResponse(responseCode = "400", description = "잘못된 요청",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class)))
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageConversionException.class)
    public ErrorResponse handleHttpMessageConversionException(HttpMessageConversionException e)
    {
        return ErrorResponse.builder()
                .code(ErrorCode.JSON_PARSE_ERROR)
                .message(ErrorCode.JSON_PARSE_ERROR.getMessage())
                .build();
    }

    @ApiResponse(responseCode = "406", description = "필드 오류",
            content = @Content(mediaType = "application/json",
                    array = @ArraySchema(
                            schema = @Schema(implementation = ErrorResponse.class))))
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    @ExceptionHandler(BindException.class)
    public List<ErrorResponse> handleBindException(BindException e)
    {
        List<FieldError> errors = e.getFieldErrors();
        return errors.stream().map(error -> ErrorResponse.builder()
                .code(ErrorCode.ARGUMENT_NOT_VALID)
                .message(error.getField() + " : " + error.getDefaultMessage())
                .build()).collect(Collectors.toList());
    }
}
