package com.example.GameCaro.domain;

import com.example.GameCaro.util.constant.PlayerEnum;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "move")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Move {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;

    @Column(name = "row_index", nullable = false)
    private byte rowIndex;

    @Column(name = "col_index", nullable = false)
    private byte colIndex;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PlayerEnum player;

    @Column(name = "move_number", nullable = false)
    private int moveNumber;
}