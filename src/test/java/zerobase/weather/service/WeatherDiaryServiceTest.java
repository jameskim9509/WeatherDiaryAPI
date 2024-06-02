package zerobase.weather.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import zerobase.weather.domain.Diary;
import zerobase.weather.domain.Weather;
import zerobase.weather.dto.UpdateDiary;
import zerobase.weather.exception.WeatherDiaryException;
import zerobase.weather.repository.DiaryRepository;
import zerobase.weather.repository.WeatherRepository;
import zerobase.weather.type.ErrorCode;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class WeatherDiaryServiceTest {
    @Mock
    DiaryRepository diaryRepository;

    @Mock
    WeatherRepository weatherRepository;

    @InjectMocks
    WeatherDiaryService weatherDiaryService;

    @DisplayName("Diary 생성 성공")
    @Test
    void createDiary() {
        //given
        String text = "123";
        Mockito.when(weatherRepository.findById(any()))
                .thenReturn(Optional.of(
                        Weather.builder()
                        .date(LocalDate.now())
                        .main("123")
                        .icon("123")
                        .temp(123D)
                        .build()
                ));

        //when
        weatherDiaryService.createDiary(LocalDate.now(), text);

        //then
        ArgumentCaptor<Diary> diaryCaptor = ArgumentCaptor.forClass(Diary.class);
        Mockito.verify(diaryRepository,Mockito.times(1))
                .save(diaryCaptor.capture());

        Diary savedDiary = diaryCaptor.getValue();
        Assertions.assertNotNull(savedDiary.getWeather());
        Assertions.assertEquals(LocalDate.now(), savedDiary.getWeather().getDate());
        Assertions.assertEquals("123", savedDiary.getText());
    }

    @DisplayName("해당 날짜의 Diary 조회 성공")
    @Test
    void readDiary() {
        Weather weather = Weather.builder()
                        .main("123")
                        .date(LocalDate.now())
                        .temp(123D)
                        .icon("123")
                        .build();
        Mockito.when(diaryRepository.findDiary(any()))
                .thenReturn(
                        List.of(
                                Diary.builder()
                                        .text("123")
                                        .weather(weather)
                                        .build(),
                                Diary.builder()
                                        .text("456")
                                        .weather(weather)
                                        .build()
                        )
                );

        List<Object> responseList
                = weatherDiaryService.readDiary(LocalDate.now());

        Assertions.assertEquals(2, responseList.size());
    }

    @DisplayName("기간 내 모든 Diary 조회 성공")
    @Test
    void readDiaries() {
        Weather weather = Weather.builder()
                .main("123")
                .date(LocalDate.now())
                .temp(123D)
                .icon("123")
                .build();
        Mockito.when(diaryRepository.findDiaries(any(), any()))
                .thenReturn(
                        List.of(
                                Diary.builder()
                                        .weather(weather)
                                        .text("123")
                                        .build(),
                                Diary.builder()
                                        .weather(weather)
                                        .text("456")
                                        .build()
                        )
                );

        List<Object> responseList = weatherDiaryService.readDiaries(
                LocalDate.now().minusDays(1), LocalDate.now()
        );

        Assertions.assertEquals(2, responseList.size());
    }

    @DisplayName("Diary 수정 성공")
    @Test
    void updateDiary() {
        Weather weather = Weather.builder()
                .main("123")
                .date(LocalDate.now())
                .temp(123D)
                .icon("123")
                .build();
        Mockito.when(diaryRepository.findDiary(any()))
                .thenReturn(
                        List.of(
                                Diary.builder()
                                        .text("123")
                                        .weather(weather)
                                        .build(),
                                Diary.builder()
                                        .text("456")
                                        .weather(weather)
                                        .build()
                        )
                );

        UpdateDiary.Response response =
                (UpdateDiary.Response) weatherDiaryService.updateDiary(LocalDate.now(), "567");

        Assertions.assertEquals("123", response.getMain());
        Assertions.assertEquals(123D, response.getTemp());
        Assertions.assertEquals("123", response.getIcon());
        Assertions.assertEquals("567", response.getText());
    }

    @DisplayName("Diary 삭제 성공")
    @Test
    void deleteDiary() {
        Weather weather = Weather.builder()
                .main("123")
                .date(LocalDate.now())
                .temp(123D)
                .icon("123")
                .build();
        Mockito.when(diaryRepository.findDiary(any()))
                .thenReturn(
                        List.of(
                                Diary.builder()
                                        .text("123")
                                        .weather(weather)
                                        .build(),
                                Diary.builder()
                                        .text("456")
                                        .weather(weather)
                                        .build()
                        )
                );
        ArgumentCaptor<Diary> diaryCaptor = ArgumentCaptor.forClass(Diary.class);

        weatherDiaryService.deleteDiary(LocalDate.now());

        Mockito.verify(diaryRepository, Mockito.times(2))
                .delete(diaryCaptor.capture());
        Assertions.assertEquals(2, diaryCaptor.getAllValues().size());
    }

    @DisplayName("해당 날짜의 Diary 조회 실패 확인")
    @Test
    void findDiaryErrorTest()
    {
        Mockito.when(diaryRepository.findDiary(any()))
                .thenReturn(new ArrayList<>());

        WeatherDiaryException ex = Assertions.assertThrows(
                WeatherDiaryException.class,
                () -> weatherDiaryService.getDiaries(LocalDate.now())
        );

        Assertions.assertEquals(ErrorCode.DIARY_NOT_FOUND, ex.getErrorCode());
    }

    @DisplayName("기간 내 Diary 조회 실패 확인")
    @Test
    void findDiariesErrorTest()
    {
        Mockito.when(diaryRepository.findDiaries(any(), any()))
                .thenReturn(new ArrayList<>());

        WeatherDiaryException ex = Assertions.assertThrows(
                WeatherDiaryException.class,
                () -> weatherDiaryService.readDiaries(any(), any())
        );

        Assertions.assertEquals(ErrorCode.DIARY_NOT_FOUND, ex.getErrorCode());
    }

    @DisplayName("날씨 조회 실패 확인")
    @Test
    void findWeatherByIdErrorTest()
    {
        String text = "123";
        Mockito.when(weatherRepository.findById(any()))
                .thenReturn(Optional.empty());

        WeatherDiaryException ex = Assertions.assertThrows(
                WeatherDiaryException.class,
                () -> weatherDiaryService.createDiary(LocalDate.now(), text)
        );

        Assertions.assertEquals(ErrorCode.WEATHER_NOT_FOUND, ex.getErrorCode());
    }
}