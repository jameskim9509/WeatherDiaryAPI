package zerobase.weather.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import zerobase.weather.domain.Diary;
import zerobase.weather.domain.Weather;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class WeatherDiaryRepositoryTest {
    @Autowired
    private DiaryRepository diaryRepository;
    @Autowired
    private WeatherRepository weatherRepository;
    @PersistenceContext
    private EntityManager em;

    Weather weather1 = Weather.builder()
            // 미래의 날짜 저장 (중복되지 않는 기본 키)
            .date(LocalDate.now().plusDays(1))
            .main("123")
            .temp(123D)
            .icon("123")
            .build();

    Weather weather2 = Weather.builder()
            // 미래의 날짜 저장 (중복되지 않는 기본 키)
            .date(LocalDate.now().plusDays(2))
            .main("456")
            .temp(456D)
            .icon("456")
            .build();

    Diary diary1 = Diary.builder()
            .text("1234")
            .weather(weather1)
            .build();

    Diary diary2 = Diary.builder()
            .text("2345")
            .weather(weather2)
            .build();

    @DisplayName("Diary Repository CRUD 성공")
    @Test
    void DiaryRepositoryCRUDTest()
    {
        Diary savedDiary1 = diaryRepository.save(diary1);
        Diary savedDiary2 = diaryRepository.save(diary2);

        Diary findDiary1 = diaryRepository.findById(savedDiary1.getId()).get();
        Diary findDiary2 = diaryRepository.findById(savedDiary2.getId()).get();

        Assertions.assertEquals(savedDiary1, findDiary1);
        Assertions.assertEquals(savedDiary2, findDiary2);

        List<Diary> allDiaries = diaryRepository.findAll();

        Assertions.assertTrue(allDiaries.contains(savedDiary1));
        Assertions.assertTrue(allDiaries.contains(savedDiary2));

        diaryRepository.delete(diary1);
        diaryRepository.delete(diary2);

        findDiary1 = diaryRepository.findById(diary1.getId()).orElse(null);
        findDiary2 = diaryRepository.findById(diary2.getId()).orElse(null);

        Assertions.assertNull(findDiary1);
        Assertions.assertNull(findDiary2);
    }

    @DisplayName("Weather Repository CRUD 성공")
    @Test
    void weatherRepositoryCRUDTest()
    {
        Weather savedWeather1 = weatherRepository.save(weather1);
        Weather savedWeather2 = weatherRepository.save(weather2);

        Weather findWeather1 = weatherRepository.findById(savedWeather1.getDate()).get();
        Weather findWeather2 = weatherRepository.findById(savedWeather2.getDate()).get();

        Assertions.assertEquals(savedWeather1, findWeather1);
        Assertions.assertEquals(savedWeather2, findWeather2);

        // 내부적으로 em.flush() 수행하는것 같다
        // ( em.clear()는 안시킨다. -> 영속성 컨텍스트 유지 )
        List<Weather> allWeathers = weatherRepository.findAll();

        Assertions.assertTrue(allWeathers.contains(savedWeather1));
        Assertions.assertTrue(allWeathers.contains(savedWeather2));

        weatherRepository.delete(savedWeather1);
        weatherRepository.delete(savedWeather2);

        findWeather1 = weatherRepository.findById(savedWeather1.getDate()).orElse(null);
        findWeather2 = weatherRepository.findById(savedWeather2.getDate()).orElse(null);

        Assertions.assertNull(findWeather1);
        Assertions.assertNull(findWeather2);
    }

    @DisplayName("Diary Repository findDiary 성공")
    @Test
    void findDiaryTest()
    {
        Diary diary3 = Diary.builder()
                .text("123")
                .weather(weather1)
                .build();

        // cascade -> weather 정보도 자동 save
        Diary savedDiary1 = diaryRepository.save(diary1);
        Diary savedDiary3 = diaryRepository.save(diary3);

        List<Diary> diaryList = diaryRepository.findDiary(LocalDate.now().plusDays(1));
        Assertions.assertEquals(2, diaryList.size());
    }

    @DisplayName("Diary Repository findDiaries 성공")
    @Test
    void findDiariesTest()
    {
        Diary diary3 = Diary.builder()
                .text("123")
                .weather(weather1)
                .build();

        // cascade -> weather 정보도 자동 save
        Diary savedDiary1 = diaryRepository.save(diary1);
        Diary savedDiary2 = diaryRepository.save(diary2);
        Diary savedDiary3 = diaryRepository.save(diary3);

        List<Diary> diaryList =
                diaryRepository.findDiaries(
                        LocalDate.now().plusDays(1),
                        LocalDate.now().plusDays(2)
                );

        Assertions.assertEquals(3, diaryList.size());
    }
}
