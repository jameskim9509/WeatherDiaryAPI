package zerobase.weather.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import zerobase.weather.aop.Logging;
import zerobase.weather.dto.*;
import zerobase.weather.service.WeatherDiaryService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@Logging
@Tag(name = "날씨 일기 API", description = "Weather Diary API")
public class WeatherDiaryController {
    public final WeatherDiaryService weatherDiaryService;

    @Operation(summary = "일기 생성", description = "해당하는 날짜의 일기를 생성합니다.",
        responses = {
            @ApiResponse(responseCode = "200", description = "일기 생성 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CreateDiary.Response.class)
                    )
            )
        })
    @PostMapping("/create/diary")
    public Object createDiary(
            @ParameterObject @ModelAttribute @Validated CreateDiary.Request reqForm,
            @RequestBody(description = "내용")
            @org.springframework.web.bind.annotation.RequestBody(required = false) String text
    )
    {
        return weatherDiaryService.createDiary(reqForm.getDate(), text);
    }

    @Operation(summary = "하루 일기 조회", description = "해당하는 날짜의 일기를 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "일기 조회 성공",
                            content = @Content(mediaType = "application/json",
                                    array = @ArraySchema(
                                        schema = @Schema(implementation = ReadDiary.Response.class)
                                    )
                            )
                    )
            })
    @GetMapping("/read/diary")
    public List<Object> readDiary(
            @ParameterObject @ModelAttribute @Validated ReadDiary.Request reqForm
    )
    {
        return weatherDiaryService.readDiary(reqForm.getDate());
    }

    @Operation(summary = "기간 내 일기 조회", description = "해당하는 기간내의 일기를 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "일기 조회 성공",
                            content = @Content(mediaType = "application/json",
                                    array = @ArraySchema(
                                        schema = @Schema(implementation = ReadDiaries.Response.class)
                                    )
                            )
                    )
            })
    @GetMapping("/read/diaries")
    public List<Object> readDiaries(
            @ParameterObject @ModelAttribute @Validated ReadDiaries.Request reqForm
    )
    {
        return weatherDiaryService.readDiaries(reqForm.getStartDate(), reqForm.getEndDate());
    }

    @Operation(summary = "일기 수정", description = "해당하는 날짜의 일기를 수정합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "일기 수정 성공",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = UpdateDiary.Response.class)
                            )
                    )
            })
    @PutMapping("/update/diary")
    public Object updateDiary(
            @ParameterObject @ModelAttribute @Validated UpdateDiary.Request reqForm,
            @RequestBody(description = "내용")
            @org.springframework.web.bind.annotation.RequestBody(required = false) String text
    )
    {
        return weatherDiaryService.updateDiary(reqForm.getDate(), text);
    }

    @Operation(summary = "일기 삭제", description = "해당하는 날짜의 일기를 삭제합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "일기 삭제 성공",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = DeleteDiary.Response.class)
                            )
                    )
            })
    @DeleteMapping("/delete/diary")
    public Object deleteDiary(
            @ParameterObject @ModelAttribute @Validated DeleteDiary.Request reqForm
    )
    {
        return weatherDiaryService.deleteDiary(reqForm.getDate());
    }
}
