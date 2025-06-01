package com.example.GameCaro.controller;

import com.example.GameCaro.domain.Game;
import com.example.GameCaro.domain.Series;
import com.example.GameCaro.domain.User;
import com.example.GameCaro.repository.SeriesRepository;
import com.example.GameCaro.repository.UserRepository;
import com.example.GameCaro.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/games")
@RequiredArgsConstructor
public class GameController {

    private final GameService gameService;
    private final UserRepository userRepository;
    private final SeriesRepository seriesRepository;

    @GetMapping
    public List<Game> getAll() {
        return gameService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Game> getById(@PathVariable Long id) {
        return gameService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/series/{seriesId}")
    public ResponseEntity<List<Game>> getGamesBySeries(@PathVariable Long seriesId, Authentication auth) {
        String username = auth.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Series series = seriesRepository.findById(seriesId)
                .orElseThrow(() -> new RuntimeException("Series not found"));

        if (!series.getUser().getId().equals(user.getId())) {
            // User không phải chủ sở hữu series -> từ chối truy cập
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        List<Game> games = gameService.getBySeriesId(seriesId);
        return ResponseEntity.ok(games);
    }



    @PostMapping
    public ResponseEntity<Game> createGame(@RequestBody Game game, Authentication auth) {
        String username = auth.getName();
        User user = userRepository.findByUsername(username).orElseThrow();

        // Kiểm tra series thuộc user không
        Series series = seriesRepository.findById(game.getSeries().getId())
                .orElseThrow(() -> new RuntimeException("Series not found"));

        if (!series.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        game.setSeries(series);
        Game savedGame = gameService.save(game);
        return ResponseEntity.ok(savedGame);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Game> updateGame(@PathVariable Long id, @RequestBody Game updatedGame, Authentication auth) {
        String username = auth.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return gameService.getById(id).map(game -> {
            if (!game.getSeries().getUser().getId().equals(user.getId())) {
                // Trả về 403 với kiểu ResponseEntity<Game>
                return ResponseEntity.status(HttpStatus.FORBIDDEN).<Game>build();
            }

            game.setStatus(updatedGame.getStatus());
            game.setWinner(updatedGame.getWinner());

            gameService.save(game);
            return ResponseEntity.ok(game);
        }).orElse(ResponseEntity.<Game>notFound().build());
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        gameService.delete(id);
        return ResponseEntity.noContent().build();
    }
}