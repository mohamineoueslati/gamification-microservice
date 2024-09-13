package com.example.gamification.game.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BadgeCard {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long badgeId;
    private Long userId;
    @EqualsAndHashCode.Exclude
    private long badgeTimestamp;
    @Enumerated(EnumType.STRING)
    private BadgeType badgeType;

    public BadgeCard(final Long userId, final BadgeType badgeType) {
        this(null, userId, System.currentTimeMillis(), badgeType);
    }

}
