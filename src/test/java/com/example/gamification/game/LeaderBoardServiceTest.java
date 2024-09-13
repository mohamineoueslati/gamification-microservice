package com.example.gamification.game;

import com.example.gamification.game.domain.BadgeCard;
import com.example.gamification.game.domain.BadgeType;
import com.example.gamification.game.domain.LeaderBoardRow;
import com.example.gamification.game.dto.TotalScoreDTO;
import com.example.gamification.game.repository.BadgeRepository;
import com.example.gamification.game.repository.ScoreRepository;
import com.example.gamification.game.service.LeaderBoardServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class LeaderBoardServiceTest {

    private LeaderBoardServiceImpl leaderBoardService;
    @Mock
    private ScoreRepository scoreRepository;
    @Mock
    private BadgeRepository badgeRepository;

    @BeforeEach
    public void setUp() {
        leaderBoardService = new LeaderBoardServiceImpl(scoreRepository, badgeRepository);
    }

    @Test
    public void retrieveLeaderBoardTest() {
        // given
        long userId1 = 1L, userId2 = 2L;
        TotalScoreDTO totalScore1 = new TotalScoreDTO(userId1, 60L);
        TotalScoreDTO totalScore2 = new TotalScoreDTO(userId2, 10L);
        BadgeCard badgeCard1 = new BadgeCard(userId1, BadgeType.FIRST_WON);
        BadgeCard badgeCard2 = new BadgeCard(userId2, BadgeType.FIRST_WON);
        BadgeCard badgeCard3 = new BadgeCard(userId1, BadgeType.BRONZE);
        given(scoreRepository.getSortedTotalScores()).willReturn(List.of(totalScore1, totalScore2));
        given(badgeRepository.findAll()).willReturn(List.of(badgeCard1, badgeCard2, badgeCard3));
        // when
        List<LeaderBoardRow> leaderBoardRows = leaderBoardService.getCurrentLeaderBoard();
        // then
        List<LeaderBoardRow> expectedLeaderBoardRows = List.of(
                new LeaderBoardRow(userId1, 60L, List.of(
                        badgeCard1.getBadgeType().getDescription(),
                        badgeCard3.getBadgeType().getDescription()
                )),
                new LeaderBoardRow(userId2, 10L, List.of(badgeCard2.getBadgeType().getDescription()))
        );
        then(leaderBoardRows).isEqualTo(expectedLeaderBoardRows);
    }

}
