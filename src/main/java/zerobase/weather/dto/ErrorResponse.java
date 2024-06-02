package zerobase.weather.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import zerobase.weather.type.ErrorCode;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    @Schema(example = "DIARY_NOT_FOUND")
    private ErrorCode code;
    @Schema(example = "해당하는 날짜에 일기가 없습니다.")
    private String message;
}
