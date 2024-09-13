package com.example.gamification.game.service;

import com.example.gamification.game.domain.BadgeCard;
import com.example.gamification.game.domain.LeaderBoardRow;
import com.example.gamification.game.dto.TotalScoreDTO;
import com.example.gamification.game.repository.BadgeRepository;
import com.example.gamification.game.repository.ScoreRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class LeaderBoardServiceImpl implements LeaderBoardService {

    private final ScoreRepository scoreRepository;
    private final BadgeRepository badgeRepository;

    @Override
    public List<LeaderBoardRow> getCurrentLeaderBoard() {
        // Get score only
        List<TotalScoreDTO> totalScores = scoreRepository.getSortedTotalScores();
        List<BadgeCard> badgeCards = badgeRepository.findAll();
        // Combine with badges
        return totalScores.stream().map(totalScore -> new LeaderBoardRow(
                totalScore.userId(),
                totalScore.totalScore(),
                badgeCards
                        .stream()
                        .filter(badgeCard -> badgeCard.getUserId().equals(totalScore.userId()))
                        .map(badgeCard -> badgeCard.getBadgeType().getDescription())
                        .toList()
        )).toList();
    }
}
