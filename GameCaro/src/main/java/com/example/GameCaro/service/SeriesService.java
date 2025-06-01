package com.example.GameCaro.service;


import com.example.GameCaro.domain.Series;
import com.example.GameCaro.repository.SeriesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SeriesService {

    private final SeriesRepository seriesRepository;

    public List<Series> getAll() {
        return seriesRepository.findAll();
    }

    public Optional<Series> getById(Long id) {
        return seriesRepository.findById(id);
    }

    public Series save(Series series) {
        return seriesRepository.save(series);
    }

    public void delete(Long id) {
        seriesRepository.deleteById(id);
    }
}