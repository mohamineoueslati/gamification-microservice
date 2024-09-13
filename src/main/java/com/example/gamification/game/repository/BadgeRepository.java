package com.example.gamification.game.repository;

import com.example.gamification.game.domain.BadgeCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BadgeRepository extends JpaRepository<BadgeCard, Long> {

    /**
     * Retrieves all BadgeCards for a given user.
     *
     * @param userId the id of the user to look for BadgeCards
     * @return the list of BadgeCards, ordered by most recent first.
     */
    List<BadgeCard> findByUserIdOrderByBadgeTimestampDesc(Long userId);

}
