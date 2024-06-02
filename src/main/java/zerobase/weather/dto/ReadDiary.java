package zerobase.weather.dto;

import com.sun.istack.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;
import zerobase.weather.validator.DatePeriod;

import java.time.LocalDate;

public class ReadDiary {
    @Schema(name = "ReadDiary_Request")
    @AllArgsConstructor
    @Builder
    @Getter
    public static class Request
    {
        @Schema(description = "조회할 날짜", required = true)
        @NotNull
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        @DatePeriod
        LocalDate date;
    }

    @Schema(name = "ReadDiary_Response")
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
