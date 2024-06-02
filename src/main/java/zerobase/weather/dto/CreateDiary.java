package zerobase.weather.dto;

import com.sun.istack.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;
import zerobase.weather.validator.DatePeriod;

import java.time.LocalDate;

public class CreateDiary {
    @Schema(name = "CreateDiary_Request")
    @AllArgsConstructor
    @Getter
    @Builder
    public static class Request
    {
        @Schema(description = "생성할 날짜", required = true)
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        @NotNull
        @DatePeriod
        LocalDate date;
    }

    @Schema(name = "CreateDiary_Response")
    @Builder
    @Getter
    @AllArgsConstructor
    public static class Response
    {
        String main;
        Double temp;
        String icon;

        String text;
    }
}
