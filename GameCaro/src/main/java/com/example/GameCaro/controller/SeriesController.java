package com.example.GameCaro.controller;

import com.example.GameCaro.domain.Series;
import com.example.GameCaro.domain.User;
import com.example.GameCaro.repository.SeriesRepository;
import com.example.GameCaro.repository.UserRepository;
import com.example.GameCaro.service.SeriesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/series")
@RequiredArgsConstructor
public class SeriesController {

    private final SeriesService seriesService;
    private final UserRepository userRepository;
    private final SeriesRepository seriesRepository;

    @GetMapping
    public List<Series> getAll() {
        return seriesService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Series> getById(@PathVariable Long id) {
        return seriesService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/mine")
    public ResponseEntity<List<Series>> getMySeries(Authentication authentication) {
        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Series> seriesList = seriesRepository.findByUserId(user.getId());
        return ResponseEntity.ok(seriesList);
    }

    @PostMapping
    public ResponseEntity<Series> create(@RequestBody Series series, Authentication authentication) {
        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        series.setUser(user);  // Gán user đang đăng nhập vào series
        Series savedSeries = seriesService.save(series);
        return ResponseEntity.ok(savedSeries);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Series> update(@PathVariable Long id, @RequestBody Series updatedSeries) {
        return seriesService.getById(id)
                .map(series -> {
                    series.setPlayer1Name(updatedSeries.getPlayer1Name());
                    series.setPlayer2Name(updatedSeries.getPlayer2Name());
                    series.setPlayer1Wins(updatedSeries.getPlayer1Wins());
                    series.setPlayer2Wins(updatedSeries.getPlayer2Wins());
                    series.setLastGameStatus(updatedSeries.getLastGameStatus());
                    series.setUser(updatedSeries.getUser());
                    return ResponseEntity.ok(seriesService.save(series));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        seriesService.delete(id);
        return ResponseEntity.noContent().build();
    }
}