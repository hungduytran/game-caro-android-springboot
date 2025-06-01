package com.example.GameCaro.repository;

import com.example.GameCaro.domain.Series;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface SeriesRepository extends JpaRepository<Series, Long>, JpaSpecificationExecutor<Series> {
    List<Series> findByUserId(Long userId);
}
