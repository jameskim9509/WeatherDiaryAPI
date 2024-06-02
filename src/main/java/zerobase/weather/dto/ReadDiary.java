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

public class ReadDiary {
    @Schema(name = "ReadDiary_Request")
    @AllArgsConstructor
    @Getter
    public static class Request
    {
        @Schema(description = "조회할 날짜", required = true, example = "2024-06-03")
        @NotNull
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        @DatePeriod
        LocalDate date;
    }

    @Schema(name = "ReadDiary_Response")
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
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
