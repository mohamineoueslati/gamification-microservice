package com.example.gamification.game.dto;

import com.example.gamification.game.domain.BadgeType;

import java.util.List;

public record GameResultDTO(int score, List<BadgeType> badges) {
}
