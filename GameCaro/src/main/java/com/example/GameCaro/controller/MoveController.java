package com.example.GameCaro.controller;

import com.example.GameCaro.domain.Move;
import com.example.GameCaro.service.MoveService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/moves")
@RequiredArgsConstructor
public class MoveController {

    private final MoveService moveService;

    @GetMapping
    public List<Move> getAll() {
        return moveService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Move> getById(@PathVariable Long id) {
        return moveService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/game/{gameId}")
    public List<Move> getByGameId(@PathVariable Long gameId) {
        return moveService.getByGameId(gameId);
    }

    @PostMapping
    public Move create(@RequestBody Move move) {
        return moveService.save(move);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Move> update(@PathVariable Long id, @RequestBody Move updatedMove) {
        return moveService.getById(id)
                .map(move -> {
                    move.setRowIndex(updatedMove.getRowIndex());
                    move.setColIndex(updatedMove.getColIndex());
                    move.setPlayer(updatedMove.getPlayer());
                    move.setMoveNumber(updatedMove.getMoveNumber());
                    move.setGame(updatedMove.getGame());
                    return ResponseEntity.ok(moveService.save(move));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        moveService.delete(id);
        return ResponseEntity.noContent().build();
    }
}