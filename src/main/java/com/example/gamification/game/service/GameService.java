package com.example.gamification.game.service;

import com.example.gamification.game.event.ChallengeSolvedEvent;
import com.example.gamification.game.dto.GameResultDTO;

/**
 * This service includes the main logic for gamifying the system.
 */
public interface GameService {
    /**
     * Process a new attempt from a given user.
     *
     * @param challenge the challenge data with user details, factors, etc.
     * @return a {@link GameResultDTO} object containing the new score and badge cards obtained
     */
    GameResultDTO newAttemptForUser(ChallengeSolvedEvent challenge);
}
