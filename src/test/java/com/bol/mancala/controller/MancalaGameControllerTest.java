package com.bol.mancala.controller;

import com.bol.mancala.model.MancalaGame;
import com.bol.mancala.model.dto.SowDto;
import com.bol.mancala.repository.MancalaGameRepository;
import com.bol.mancala.service.MancalaGameBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest()
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
public class MancalaGameControllerTest {
    private static String gameInstanceJsonString;
    private static String sowEndpointJsonString;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MancalaGameBuilder gameBuilder;

    @MockBean
    private MancalaGameRepository repository;


    @BeforeAll
    static void init() throws IOException {
        gameInstanceJsonString = getJsonString("/game-instance-endpoint.json");
        sowEndpointJsonString = getJsonString("/sow-endpoint.json");
    }

    @Test
    @DisplayName(value = "Test createGameInstance endpoint")
    void testGameInstanceEndpoint() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/api/mancala-game/start/6")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.playerTurn").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.currentPitIndex").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.pits").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.pits[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.pits[0].stones").value(6))
                .andExpect(MockMvcResultMatchers.jsonPath("$.pits[6].id").value(7))
                .andExpect(MockMvcResultMatchers.jsonPath("$.pits[6].stones").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.pits[13].id").value(14))
                .andExpect(MockMvcResultMatchers.jsonPath("$.pits[13].stones").value(0));
    }

    @BeforeEach
    void setup() {
        MancalaGame game = this.gameBuilder.getNew(6);

        when(this.repository.get(any())).thenReturn(game);
    }

    @Test
    @DisplayName(value = "Test sow endpoint")
    void testSowEndpoint() throws Exception {
        Integer pitId = 1;
        MancalaGame game = this.gameBuilder.getNew(6);

        when(this.repository.get(any())).thenReturn(game);

        SowDto sowDto = new SowDto(game.getId(), pitId);


        mockMvc.perform(MockMvcRequestBuilders.post("/api/mancala-game/sow")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(sowDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.pits").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.currentPitIndex").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.playerTurn").isNotEmpty());
    }

    @Test
    @DisplayName(value = "Test last-state endpoint")
    void testLastStateEndpoint() throws Exception {
        MancalaGame game = this.gameBuilder.getNew(6);

        when(this.repository.get(any())).thenReturn(game);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/mancala-game/last-state/{gameId}", game.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.playerTurn").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.currentPitIndex").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.pits").isNotEmpty());
    }

    private static String getJsonString(String path) throws IOException {
        File file = new ClassPathResource(path).getFile();
        return new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);
    }
}
