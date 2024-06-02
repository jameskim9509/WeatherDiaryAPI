package zerobase.weather.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import zerobase.weather.dto.CreateDiary;
import zerobase.weather.dto.ReadDiaries;
import zerobase.weather.dto.ReadDiary;
import zerobase.weather.dto.UpdateDiary;
import zerobase.weather.exception.WeatherDiaryException;
import zerobase.weather.service.WeatherDiaryService;
import zerobase.weather.type.ErrorCode;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
class WeatherDiaryControllerTest {
    @MockBean
    WeatherDiaryService weatherDiaryService;

    @Autowired
    private MockMvc mockMvc;

    @DisplayName("Diary 생성 성공")
    @Test
    void createDiary() throws Exception {
        String text = "123";
        Mockito.when(weatherDiaryService.createDiary(any(), any()))
                .thenReturn(
                        CreateDiary.Response.builder()
                                .text(text)
                                .icon("123")
                                .main("123")
                                .temp(123D)
                                .build()
                );

        mockMvc.perform(post("/create/diary?date="+ LocalDate.now())
                .content(text))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text").value(text))
                .andExpect(jsonPath("$.icon").exists())
                .andExpect(jsonPath("$.main").exists())
                .andExpect(jsonPath("$.temp").exists())
        ;
    }

    @DisplayName("하루 Diary 조회 성공")
    @Test
    void readDiary() throws Exception {
        String text = "123";
        Mockito.when(weatherDiaryService.readDiary(any()))
                .thenReturn(
                        List.of(
                                ReadDiary.Response.builder()
                                        .text(text)
                                        .icon("123")
                                        .main("123")
                                        .temp(123D)
                                        .build(),
                                ReadDiary.Response.builder()
                                        .text(text)
                                        .icon("123")
                                        .main("123")
                                        .temp(123D)
                                        .build()
                        )
                );

        mockMvc.perform(get("/read/diary?date="+ LocalDate.now()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].text").value(text))
                .andExpect(jsonPath("$[0].icon").exists())
                .andExpect(jsonPath("$[0].main").exists())
                .andExpect(jsonPath("$[0].temp").exists())
                .andExpect(jsonPath("$[1].text").value(text))
                .andExpect(jsonPath("$[1].icon").exists())
                .andExpect(jsonPath("$[1].main").exists())
                .andExpect(jsonPath("$[1].temp").exists());
    }

    @DisplayName("기간 내 Diary 조회 성공")
    @Test
    void readDiaries() throws Exception {
        String text = "123";
        Mockito.when(weatherDiaryService.readDiaries(any(), any()))
                .thenReturn(
                        List.of(
                                ReadDiaries.Response.builder()
                                        .text(text)
                                        .icon("123")
                                        .main("123")
                                        .temp(123D)
                                        .build(),
                                ReadDiaries.Response.builder()
                                        .text(text)
                                        .icon("123")
                                        .main("123")
                                        .temp(123D)
                                        .build()
                        )
                );

        mockMvc.perform(get("/read/diaries?")
                .param("startDate", String.valueOf(LocalDate.now().minusDays(1)))
                .param("endDate", String.valueOf(LocalDate.now())))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].text").value(text))
                .andExpect(jsonPath("$[0].icon").exists())
                .andExpect(jsonPath("$[0].main").exists())
                .andExpect(jsonPath("$[0].temp").exists())
                .andExpect(jsonPath("$[1].text").value(text))
                .andExpect(jsonPath("$[1].icon").exists())
                .andExpect(jsonPath("$[1].main").exists())
                .andExpect(jsonPath("$[1].temp").exists());
    }

    @DisplayName("Diary 수정 성공")
    @Test
    void updateDiary() throws Exception {
        String text = "123";
        Mockito.when(weatherDiaryService.updateDiary(any(), any()))
                .thenReturn(
                        UpdateDiary.Response.builder()
                                .text(text)
                                .icon("123")
                                .main("123")
                                .temp(123D)
                                .build()
                );

        mockMvc.perform(put("/update/diary")
                        .param("date", String.valueOf(LocalDate.now()))
                        .content(text))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text").value(text))
                .andExpect(jsonPath("$.icon").exists())
                .andExpect(jsonPath("$.main").exists())
                .andExpect(jsonPath("$.temp").exists());
    }

    @DisplayName("Diary 삭제 성공")
    @Test
    void deleteDiary() throws Exception {
        String text = "123";
        Mockito.when(weatherDiaryService.deleteDiary(any()))
                .thenReturn("deleted");

        mockMvc.perform(delete("/delete/diary")
                        .param("date", String.valueOf(LocalDate.now())))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("deleted"));
    }

    @DisplayName("Diary 생성 오류 확인")
    @Test
    void createDiaryExceptionTest() throws Exception {
        Mockito.when(weatherDiaryService.createDiary(any(), any()))
                .thenThrow(new WeatherDiaryException(ErrorCode.WEATHER_NOT_FOUND));

        mockMvc.perform(post("/create/diary")
                .param("date", String.valueOf(LocalDate.now())))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code")
                        .value(ErrorCode.WEATHER_NOT_FOUND.toString()))
                .andExpect(jsonPath("$.message")
                        .value(ErrorCode.WEATHER_NOT_FOUND.getMessage())
                );
    }

    @DisplayName("하루 Diary 조회 오류 확인")
    @Test
    void readDiaryExceptionTest() throws Exception {
        Mockito.when(weatherDiaryService.readDiary(any()))
                .thenThrow(new WeatherDiaryException(ErrorCode.DIARY_NOT_FOUND));

        mockMvc.perform(get("/read/diary")
                        .param("date", String.valueOf(LocalDate.now())))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code")
                        .value(ErrorCode.DIARY_NOT_FOUND.toString()))
                .andExpect(jsonPath("$.message")
                        .value(ErrorCode.DIARY_NOT_FOUND.getMessage())
                );
    }

    @DisplayName("기간내 Diary 조회 오류 확인")
    @Test
    void readDiariesExceptionTest() throws Exception {
        Mockito.when(weatherDiaryService.readDiaries(any(), any()))
                .thenThrow(new WeatherDiaryException(ErrorCode.DIARY_NOT_FOUND));

        mockMvc.perform(get("/read/diaries")
                        .param("startDate", String.valueOf(LocalDate.now().minusDays(1)))
                        .param("endDate", String.valueOf(LocalDate.now())))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code")
                        .value(ErrorCode.DIARY_NOT_FOUND.toString()))
                .andExpect(jsonPath("$.message")
                        .value(ErrorCode.DIARY_NOT_FOUND.getMessage())
                );
    }

    @DisplayName("Diary 수정 오류 확인")
    @Test
    void updateDiaryException() throws Exception {
        Mockito.when(weatherDiaryService.updateDiary(any(), any()))
                .thenThrow(new WeatherDiaryException(ErrorCode.DIARY_NOT_FOUND));

        mockMvc.perform(put("/update/diary")
                        .param("date", String.valueOf(LocalDate.now())))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code")
                        .value(ErrorCode.DIARY_NOT_FOUND.toString()))
                .andExpect(jsonPath("$.message")
                        .value(ErrorCode.DIARY_NOT_FOUND.getMessage())
                );
    }

    @DisplayName("Diary 삭제 오류 확인")
    @Test
    void deleteDiaryException() throws Exception {
        Mockito.when(weatherDiaryService.deleteDiary(any()))
                .thenThrow(new WeatherDiaryException(ErrorCode.DIARY_NOT_FOUND));

        mockMvc.perform(delete("/delete/diary")
                        .param("date", String.valueOf(LocalDate.now())))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code")
                        .value(ErrorCode.DIARY_NOT_FOUND.toString()))
                .andExpect(jsonPath("$.message")
                        .value(ErrorCode.DIARY_NOT_FOUND.getMessage())
                );
    }
}