package zerobase.weather.type;

import lombok.Getter;

@Getter
public enum ErrorCode {
    DIARY_NOT_FOUND("해당하는 날짜에 일기가 없습니다."),
    ARGUMENT_NOT_VALID(""),
    WEATHER_DATA_ERROR("날씨 데이터를 가져오지 못했습니다."),
    WEATHER_NOT_FOUND("해당하는 날짜의 날씨 데이터가 없습니다."),
    INVALID_DATE("범위를 벗어나는 날짜입니다."),
    JSON_PARSE_ERROR("올바른 JSON 형식이 아닙니다.");

    private final String message;

    ErrorCode(String message)
    {
        this.message= message;
    }
}
