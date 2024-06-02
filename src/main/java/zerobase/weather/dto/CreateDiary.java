package zerobase.weather.dto;

import com.sun.istack.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import zerobase.weather.validator.DatePeriod;

import java.time.LocalDate;

public class CreateDiary {
    @Schema(name = "CreateDiary_Request")
    @AllArgsConstructor
    @Getter
    public static class Request
    {
        @Schema(description = "생성할 날짜", required = true, example = "2024-06-03")
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        @NotNull
        @DatePeriod
        LocalDate date;
    }

    @Schema(name = "CreateDiary_Response")
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response
    {
        @Schema(example = "Clear")
        String main;
        @Schema(example = "297.11")
        Double temp;
        @Schema(example = "01n")
        String icon;

        String text;
    }
}
