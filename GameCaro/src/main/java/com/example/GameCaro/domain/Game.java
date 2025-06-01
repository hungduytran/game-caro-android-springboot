package com.example.GameCaro.domain;

import com.example.GameCaro.util.constant.GameStatusEnum;
import com.example.GameCaro.util.constant.WinnerEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "game")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "series_id", nullable = false)
    @JsonIgnore  // Bỏ qua khi serialize JSON
    private Series series;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GameStatusEnum status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WinnerEnum winner;
}