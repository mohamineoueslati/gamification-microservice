package com.example.gamification.game;

import com.example.gamification.game.controller.GameController;
import com.example.gamification.game.domain.BadgeType;
import com.example.gamification.game.dto.ChallengeSolvedDTO;
import com.example.gamification.game.dto.GameResultDTO;
import com.example.gamification.game.service.GameService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ExtendWith(SpringExtension.class)
@AutoConfigureJsonTesters
@WebMvcTest(controllers = GameController.class)
public class GameControllerTest {

    @MockBean
    private GameService gameService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JacksonTester<ChallengeSolvedDTO> jsonRequest;
    @Autowired
    private JacksonTester<GameResultDTO> jsonResponse;

    @Test
    public void postCorrectResult() throws Exception {
        // given
        var challengeSolved = new ChallengeSolvedDTO(1L, true, 5, 3, 1L, "amine");
        var expectedGameResult = new GameResultDTO(10, List.of(BadgeType.FIRST_WON));
        given(gameService.newAttemptForUser(challengeSolved)).willReturn(expectedGameResult);
        // when
        var response = mockMvc.perform(
                post("/attempts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest.write(challengeSolved).getJson())
        ).andReturn().getResponse();
        // then
        then(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        then(response.getContentAsString()).isEqualTo(jsonResponse.write(expectedGameResult).getJson());
    }

    @Test
    public void postIncorrectResult() throws Exception {
        // given
        var challengeSolved = new ChallengeSolvedDTO(1L, false, 5, 3, 1L, "amine");
        var expectedGameResult = new GameResultDTO(0, List.of());
        given(gameService.newAttemptForUser(challengeSolved)).willReturn(expectedGameResult);
        // when
        var response = mockMvc.perform(
                post("/attempts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest.write(challengeSolved).getJson())
        ).andReturn().getResponse();
        // then
        then(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        then(response.getContentAsString()).isEqualTo(jsonResponse.write(expectedGameResult).getJson());
    }

}
