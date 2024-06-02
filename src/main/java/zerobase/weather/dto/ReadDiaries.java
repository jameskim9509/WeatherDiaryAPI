package zerobase.weather.dto;

import com.sun.istack.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;
import zerobase.weather.validator.DatePeriod;

import java.time.LocalDate;

public class ReadDiaries {
    @Schema(name = "ReadDiaries_Request")
    @Getter
    @Builder
    @AllArgsConstructor
    public static class Request
    {
        @Schema(description = "조회할 시작 날짜", required = true)
        @NotNull
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        @DatePeriod
        LocalDate startDate;

        @Schema(description = "조회할 마지막 날짜", required = true)
        @NotNull
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        @DatePeriod
        LocalDate endDate;
    }

    @Schema(name = "ReadDiaries_Response")
    @Getter
    @Builder
    @AllArgsConstructor
//    @NoArgsConstructor
    public static class Response
    {
        String main;
        Double temp;
        String icon;

        String text;
    }
}
