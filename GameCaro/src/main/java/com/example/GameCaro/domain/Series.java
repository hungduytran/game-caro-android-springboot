package com.example.GameCaro.domain;

import com.example.GameCaro.util.constant.LastGameStatusEnum;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "series")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Series {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "player1_name", nullable = false, length = 100)
    private String player1Name;

    @Column(name = "player2_name", nullable = false, length = 100)
    private String player2Name;

    @Column(name = "player1_wins", nullable = false)
    private int player1Wins = 0;

    @Column(name = "player2_wins", nullable = false)
    private int player2Wins = 0;

    @Enumerated(EnumType.STRING)
    @Column(name = "last_game_status")
    private LastGameStatusEnum lastGameStatus = LastGameStatusEnum.PENDING;

}