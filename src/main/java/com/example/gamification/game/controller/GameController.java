package com.example.gamification.game.controller;

import com.example.gamification.game.dto.ChallengeSolvedDTO;
import com.example.gamification.game.dto.GameResultDTO;
import com.example.gamification.game.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/attempts")
@RequiredArgsConstructor
public class GameController {

    private final GameService gameService;

    @PostMapping
    public ResponseEntity<GameResultDTO> postResult(@RequestBody ChallengeSolvedDTO dto) {
        return ResponseEntity.ok(gameService.newAttemptForUser(dto));
    }
}
