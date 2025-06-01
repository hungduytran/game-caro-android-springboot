package com.example.GameCaro.repository;

import com.example.GameCaro.domain.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface GameRepository extends JpaRepository<Game, Long>, JpaSpecificationExecutor<Game> {
    List<Game> findBySeriesId(Long seriesId);
}
