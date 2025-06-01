package com.example.GameCaro.service;

import com.example.GameCaro.domain.Game;
import com.example.GameCaro.repository.GameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GameService {
    private final GameRepository gameRepository;

    public List<Game> getAll() {
        return gameRepository.findAll();
    }

    public Optional<Game> getById(Long id) {
        return gameRepository.findById(id);
    }

    public List<Game> getBySeriesId(Long seriesId) {
        return gameRepository.findBySeriesId(seriesId);
    }

    public Game save(Game game) {
        return gameRepository.save(game);
    }

    public void delete(Long id) {
        gameRepository.deleteById(id);
    }
}