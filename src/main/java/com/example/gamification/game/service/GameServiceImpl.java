package com.example.gamification.game.service;

import com.example.gamification.game.domain.BadgeCard;
import com.example.gamification.game.domain.BadgeType;
import com.example.gamification.game.domain.ScoreCard;
import com.example.gamification.game.dto.ChallengeSolvedDTO;
import com.example.gamification.game.dto.GameResultDTO;
import com.example.gamification.game.repository.BadgeRepository;
import com.example.gamification.game.repository.ScoreRepository;
import com.example.gamification.game.service.badgeprocessor.BadgeProcessor;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class GameServiceImpl implements GameService {

    private final ScoreRepository scoreRepository;
    private final BadgeRepository badgeRepository;
    private final List<BadgeProcessor> badgeProcessors;

    @Override
    public GameResultDTO newAttemptForUser(ChallengeSolvedDTO challenge) {
        // We give points only if it's correct
        if (challenge.correct()) {
            ScoreCard scoreCard = new ScoreCard(challenge.userId(), challenge.attemptId());
            scoreRepository.save(scoreCard);
            log.info(
                    "User {} scored {} points for attempt id {}",
                    challenge.userAlias(),
                    scoreCard.getScore(),
                    challenge.attemptId()
            );
            List<BadgeType> badgeTypes = processForBadges(challenge).stream()
                    .map(BadgeCard::getBadgeType)
                    .toList();
            return new GameResultDTO(scoreCard.getScore(), badgeTypes);
        } else {
            log.info(
                    "Attempt id {} is not correct. User {} does not get score.",
                    challenge.attemptId(),
                    challenge.userAlias()
            );
            return new GameResultDTO(0, List.of());
        }
    }

    /**
     * Checks the total score and the different scorecards obtained to give new badges in case their conditions are met.
     */
    private List<BadgeCard> processForBadges(final ChallengeSolvedDTO solvedChallenge) {
        Optional<Integer> optTotalScore = scoreRepository.getTotalScoreForUser(solvedChallenge.userId());
        if (optTotalScore.isEmpty()) return Collections.emptyList();
        int totalScore = optTotalScore.get();
        // Gets the total score and existing badges for that user
        List<ScoreCard> scoreCardList = scoreRepository.findByUserIdOrderByScoreTimestampDesc(solvedChallenge.userId());
        Set<BadgeType> alreadyGotBadges =
                badgeRepository.findByUserIdOrderByBadgeTimestampDesc(solvedChallenge.userId())
                        .stream()
                        .map(BadgeCard::getBadgeType)
                        .collect(Collectors.toSet());
        // Calls the badge processors for badges that the user doesn't have yet
        List<BadgeCard> newBadgeCards = badgeProcessors.stream()
                .filter(bp -> !alreadyGotBadges.contains(bp.badgeType()))
                .map(bp -> bp.processForOptionalBadge(totalScore, scoreCardList, solvedChallenge))
                .flatMap(Optional::stream)
                .map(badgeType -> new BadgeCard(solvedChallenge.userId(), badgeType))
                .toList();
        badgeRepository.saveAll(newBadgeCards);
        return newBadgeCards;
    }
}
