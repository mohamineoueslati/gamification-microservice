package com.example.gamification.game.service;

import com.example.gamification.game.domain.LeaderBoardRow;

import java.util.List;

public interface LeaderBoardService {

    /**
     * @return the current leader board ranked from high to low score
     */
    List<LeaderBoardRow> getCurrentLeaderBoard();

}
