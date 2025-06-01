package com.example.GameCaro.repository;

import com.example.GameCaro.domain.Move;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface MoveRepository extends JpaRepository<Move, Long>, JpaSpecificationExecutor<Move> {
    List<Move> findByGameIdOrderByMoveNumberAsc(Long gameId);
}
