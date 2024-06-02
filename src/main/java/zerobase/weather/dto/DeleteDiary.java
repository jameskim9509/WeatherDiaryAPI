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

public class DeleteDiary {
    @Schema(name = "DeleteDiary Request")
    @AllArgsConstructor
    @Getter
    public static class Request
    {
        @Schema(description = "삭제할 날짜", required = true)
        @NotNull
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        @DatePeriod
        LocalDate date;
    }

    @Schema(name = "DeleteDiary Response")
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response
    {
        String message = "deleted";
    }
}
