package com.example.gamification.game;

import com.example.gamification.game.domain.BadgeCard;
import com.example.gamification.game.domain.BadgeType;
import com.example.gamification.game.domain.ScoreCard;
import com.example.gamification.game.dto.ChallengeSolvedDTO;
import com.example.gamification.game.dto.GameResultDTO;
import com.example.gamification.game.repository.BadgeRepository;
import com.example.gamification.game.repository.ScoreRepository;
import com.example.gamification.game.service.GameServiceImpl;
import com.example.gamification.game.service.badgeprocessor.BadgeProcessor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class GameServiceTest {

    private GameServiceImpl gameService;
    @Mock
    private ScoreRepository scoreRepository;
    @Mock
    private BadgeRepository badgeRepository;
    @Mock
    private BadgeProcessor badgeProcessor;

    @BeforeEach
    public void setUp() {
        gameService = new GameServiceImpl(scoreRepository, badgeRepository, List.of(badgeProcessor));
    }

    @Test
    public void processCorrectAttempt() {
        // given
        long userId = 1L, attemptId = 10L;
        var attempt = new ChallengeSolvedDTO(attemptId, true, 20, 70, userId, "amine");
        ScoreCard scoreCard = new ScoreCard(userId, attemptId);
        given(scoreRepository.getTotalScoreForUser(userId)).willReturn(Optional.of(10));
        given(scoreRepository.findByUserIdOrderByScoreTimestampDesc(userId)).willReturn(List.of(scoreCard));
        given(badgeRepository.findByUserIdOrderByBadgeTimestampDesc(userId)).willReturn(List.of());
        given(badgeProcessor.badgeType()).willReturn(BadgeType.FIRST_WON);
        given(badgeProcessor.processForOptionalBadge(10, List.of(scoreCard), attempt))
                .willReturn(Optional.of(BadgeType.FIRST_WON));
        // when
        final var gameResult = gameService.newAttemptForUser(attempt);
        // then
        then(gameResult).isEqualTo(new GameResultDTO(10, List.of(BadgeType.FIRST_WON)));
        // verify
        verify(scoreRepository).save(scoreCard);
        verify(badgeRepository).saveAll(List.of(new BadgeCard(userId, BadgeType.FIRST_WON)));
    }

    @Test
    public void processWrongAttempt() {
        // given
        long userId = 1L, attemptId = 10L;
        var attempt = new ChallengeSolvedDTO(attemptId, false, 10, 10, userId, "amine");
        // when
        final var gameResult = gameService.newAttemptForUser(attempt);
        // then
        then(gameResult).isEqualTo(new GameResultDTO(0, List.of()));
    }

}
