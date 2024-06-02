package zerobase.weather.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import zerobase.weather.domain.Diary;

import java.time.LocalDate;
import java.util.List;

public interface DiaryRepository extends JpaRepository<Diary, Long> {
    @Query("select d from Diary d join fetch d.weather w where w.date=:date")
    List<Diary> findDiary(@Param("date")LocalDate date);

    @Query("select d from Diary d join fetch d.weather w where w.date between :startDate and :endDate")
    List<Diary> findDiaries(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
