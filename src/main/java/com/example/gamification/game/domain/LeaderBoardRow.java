package com.example.gamification.game.domain;

import java.util.List;

public record LeaderBoardRow(
        Long userId,
        Long totalScore,
        List<String> badges
) {
}
