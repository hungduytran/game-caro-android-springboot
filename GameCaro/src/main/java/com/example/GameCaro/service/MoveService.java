package com.example.GameCaro.service;

import com.example.GameCaro.domain.Move;
import com.example.GameCaro.repository.MoveRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MoveService {
    private final MoveRepository moveRepository;

    public List<Move> getAll() {
        return moveRepository.findAll();
    }

    public Optional<Move> getById(Long id) {
        return moveRepository.findById(id);
    }

    public List<Move> getByGameId(Long gameId) {
        return moveRepository.findByGameIdOrderByMoveNumberAsc(gameId);
    }

    public Move save(Move move) {
        return moveRepository.save(move);
    }

    public void delete(Long id) {
        moveRepository.deleteById(id);
    }
}
