package com.example.gamification.game.controller;

import com.example.gamification.game.domain.LeaderBoardRow;
import com.example.gamification.game.service.LeaderBoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * This class implements a REST API for the Gamification LeaderBoard service.
 */
@RestController
@RequestMapping("/leaders")
@RequiredArgsConstructor
@Slf4j
public class LeaderBoardController {

    private final LeaderBoardService leaderBoardService;

    @GetMapping
    public List<LeaderBoardRow> getLeaderBoard() {
        log.info("Retrieving leaderboard");
        return leaderBoardService.getCurrentLeaderBoard();
    }
}
