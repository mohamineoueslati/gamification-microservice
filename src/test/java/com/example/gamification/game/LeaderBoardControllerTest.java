package com.example.gamification.game;

import com.example.gamification.game.controller.LeaderBoardController;
import com.example.gamification.game.domain.BadgeType;
import com.example.gamification.game.domain.LeaderBoardRow;
import com.example.gamification.game.service.LeaderBoardService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@ExtendWith(SpringExtension.class)
@AutoConfigureJsonTesters
@WebMvcTest(controllers = LeaderBoardController.class)
public class LeaderBoardControllerTest {

    @MockBean
    private LeaderBoardService leaderBoardService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JacksonTester<List<LeaderBoardRow>> jsonResponse;

    @Test
    public void callLearBoardEndpointTest() throws Exception {
        // given
        var expectedLeaderBoardRows = List.of(
                new LeaderBoardRow(
                        1L,
                        60L,
                        List.of(BadgeType.FIRST_WON.getDescription(), BadgeType.BRONZE.getDescription())
                )
        );
        given(leaderBoardService.getCurrentLeaderBoard()).willReturn(expectedLeaderBoardRows);
        // when
        var response = mockMvc.perform(get("/leaders").contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();
        // then
        then(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        then(response.getContentAsString()).isEqualTo(jsonResponse.write(expectedLeaderBoardRows).getJson());
    }

}
