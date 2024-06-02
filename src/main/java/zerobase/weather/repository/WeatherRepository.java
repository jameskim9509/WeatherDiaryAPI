package zerobase.weather.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import zerobase.weather.domain.Weather;

import java.time.LocalDate;

public interface WeatherRepository extends JpaRepository<Weather, LocalDate> {
}
