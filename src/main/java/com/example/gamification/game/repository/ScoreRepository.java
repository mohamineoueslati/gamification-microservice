package com.example.gamification.game.repository;

import com.example.gamification.game.domain.ScoreCard;
import com.example.gamification.game.dto.TotalScoreDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ScoreRepository extends JpaRepository<ScoreCard, Long> {

    /**
     * Gets the total score for a given user: the sum of the scores of all their ScoreCards.
     * @param userId the id of the user
     * @return the total score for the user, empty if the user doesn't exist
     */
    @Query("select SUM(sc.score) from ScoreCard sc where sc.userId = :userId group by sc.userId")
    Optional<Integer> getTotalScoreForUser(@Param("userId") Long userId);

    /**
     * Retrieves all the ScoreCards for a given user, identified by his user id.
     * @param userId the id of the user
     * @return a list containing all the ScoreCards for the given user, sorted by most recent.
     */
    List<ScoreCard> findByUserIdOrderByScoreTimestampDesc(Long userId);

    /**
     * Retrieves a list of {@link TotalScoreDTO}s representing the total score by each user
     * @return the list of total scores, sorted by highest score first.
     */
    @Query("""
        select new com.example.gamification.game.dto.TotalScoreDTO(sc.userId, sum(sc.score))
        from ScoreCard sc
        group by sc.userId
        order by sum(sc.score)
    """)
    List<TotalScoreDTO> getSortedTotalScores();

}
