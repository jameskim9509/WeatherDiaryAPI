package zerobase.weather.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import zerobase.weather.aop.Logging;
import zerobase.weather.domain.Diary;
import zerobase.weather.domain.Weather;
import zerobase.weather.dto.*;
import zerobase.weather.exception.WeatherDiaryException;
import zerobase.weather.repository.DiaryRepository;
import zerobase.weather.repository.WeatherRepository;
import zerobase.weather.type.ErrorCode;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WeatherDiaryService {
    private final DiaryRepository diaryRepository;
    private final WeatherRepository weatherRepository;

    @Value("${weather.url}") String rootUrl;
    @Value("${weather.apiKey}") String apiKey;

    @Transactional
    public Object createDiary(LocalDate date, String text)
    {
        Optional<Weather> weather = weatherRepository.findById(date);
        if(weather.isEmpty())
        {
            log.error("WEATHER_NOT_FOUND");
            throw new WeatherDiaryException(ErrorCode.WEATHER_NOT_FOUND);
        }

        Diary diary = Diary.builder()
                .weather(weather.get())
                .text(text)
                .build();

        diaryRepository.save(diary);

        return CreateDiary.Response.builder()
                .text(diary.getText())
                .icon(diary.getWeather().getIcon())
                .main(diary.getWeather().getMain())
                .temp(diary.getWeather().getTemp())
                .build();
    }

    public List<Object> readDiary(LocalDate date)
    {
        List<Diary> diaryList = getDiaries(date);

        return diaryList.stream()
                .map(diary -> ReadDiary.Response.builder()
                        .main(diary.getWeather().getMain())
                        .icon(diary.getWeather().getIcon())
                        .temp(diary.getWeather().getTemp())
                        .text(diary.getText())
                        .build())
                .collect(Collectors.toList());
    }

    public List<Object> readDiaries(LocalDate startDate, LocalDate endDate)
    {
        List<Diary> diaryList = diaryRepository.findDiaries(startDate, endDate);

        if(diaryList.size() == 0)
        {
            log.error("DIARY_NOT_FOUND");
            throw new WeatherDiaryException(ErrorCode.DIARY_NOT_FOUND);
        }

        return diaryList.stream()
                .map(diary -> ReadDiaries.Response.builder()
                        .main(diary.getWeather().getMain())
                        .icon(diary.getWeather().getIcon())
                        .temp(diary.getWeather().getTemp())
                        .text(diary.getText())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional
    public Object updateDiary(LocalDate date, String text)
    {
        Diary findDiary = getDiaries(date).get(0);
        findDiary.changeText(text);

        return UpdateDiary.Response.builder()
                .main(findDiary.getWeather().getMain())
                .icon(findDiary.getWeather().getIcon())
                .temp(findDiary.getWeather().getTemp())
                .text(findDiary.getText())
                .build();
    }

    @Transactional
    public Object deleteDiary(LocalDate date)
    {
        List<Diary> diaryList = getDiaries(date);

        diaryList.stream()
                .forEach(diary -> diaryRepository.delete(diary));

        return DeleteDiary.Response.builder()
                .message("deleted")
                .build();
    }

    //------ Overlapping Code Extraction --------
    public List<Diary> getDiaries(LocalDate date) {
        List<Diary> diaryList = diaryRepository.findDiary(date);

        if(diaryList.size() == 0)
        {
            log.error("DIARY_NOT_FOUND");
            throw new WeatherDiaryException(ErrorCode.DIARY_NOT_FOUND);
        }
        return diaryList;
    }

    // ----------- Open API ---------
    private Map<String, Object> getWeatherData(String rootUrl, String apiKey)
    {
        RestTemplate restTemplate = new RestTemplate();
        String url = rootUrl + "/data/2.5/weather?q=seoul&appid=" + apiKey;
        ResponseEntity<String> response;

        try{
            response = restTemplate.getForEntity(url, String.class);
        }catch (RestClientException e)
        {
            log.error("BadResponse {}", ErrorCode.WEATHER_DATA_ERROR);
            throw new WeatherDiaryException(ErrorCode.WEATHER_DATA_ERROR);
        }


        Map<String, Object> weatherMap = new HashMap<>();
        try{
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject
                    = (JSONObject) jsonParser.parse(response.getBody());
            JSONObject mainObject
                    = (JSONObject) jsonObject.get("main");
            JSONObject weatherObject =
                    (JSONObject) ((JSONArray) jsonObject.get("weather")).get(0);

            weatherMap.put("main", weatherObject.get("main"));
            weatherMap.put("temp", mainObject.get("temp"));
            weatherMap.put("icon", weatherObject.get("icon"));
        }
        catch (ParseException pe)
        {
            log.error("ParseException {}", pe.getMessage());
            throw new WeatherDiaryException(ErrorCode.WEATHER_DATA_ERROR);
        }

        return weatherMap;
    }

    @Logging
    @Scheduled(cron = "0 0 1 * * *")
    @Transactional
    public void saveWeatherData()
    {
        Map<String, Object> weatherMap;
        String main;
        String temp;
        String icon;
        try{
            weatherMap = getWeatherData(rootUrl, apiKey);

            main = weatherMap.get("main").toString();
            temp = weatherMap.get("temp").toString();
            icon = weatherMap.get("icon").toString();
        }
        catch (NullPointerException e)
        {
            log.error("empty Component {}", e.getMessage());
            throw new WeatherDiaryException(ErrorCode.WEATHER_DATA_ERROR);
        }

        Weather weather = Weather.builder()
                .date(LocalDate.now())
                .main(main)
                .temp(Double.valueOf(temp))
                .icon(icon)
                .build();

        weatherRepository.save(weather);
    }
}
