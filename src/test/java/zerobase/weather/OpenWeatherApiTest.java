package zerobase.weather;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@TestPropertySource(locations = "classpath:application.properties")
public class OpenWeatherApiTest {
    @Value("${weather.url}")
    private String rootUrl;
    @Value("${weather.apiKey}")
    private String apiKey;

    @DisplayName("날씨 정보 얻어오기 성공")
    @Test
    void getWeatherData() throws ParseException {
        RestTemplate restTemplate = new RestTemplate();

        String url = rootUrl + "/data/2.5/weather?q=seoul&appid=" + apiKey;
        ResponseEntity<String> response
                = restTemplate.getForEntity(url, String.class);

        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(response.getBody());
        JSONObject mainObject = (JSONObject) jsonObject.get("main");
        JSONObject weatherObject =
                (JSONObject) ((JSONArray) jsonObject.get("weather")).get(0);

        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertNotNull(mainObject.get("temp"));
        assertNotNull(weatherObject.get("main"));
        assertNotNull(weatherObject.get("icon"));
    }

    @DisplayName("날씨 정보 얻어오기 실패 확인")
    @Test
    public void getWeatherDataError(){
        RestTemplate restTemplate = new RestTemplate();
        String url = rootUrl + "/data/2.5/weather?q=seoul&appid=1234";

        Assertions.assertThrows(
                RestClientException.class,
                () -> restTemplate.getForEntity(url, String.class)
        );
    }
}
