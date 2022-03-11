package bol.com.mancala.controller;

import bol.com.mancala.*;
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

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest()
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
public class GameControllerTest {

    private String gameId;
    private final int numberOfPits = 6;
    private GameModel game;
    private GameInstanceApiRequestModel gameInstanceReqModel;
    private SowApiRequestModel sowApiRequestModel;
    private static String gameInstanceJsonString;
    private static String sowEndpointJsonString;

    @MockBean
    private GameService gameService;

    @MockBean
    private CacheProvider cache;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    @BeforeAll
    static void init() throws IOException {
        gameInstanceJsonString = getJsonString("/game-instance-endpoint.json");
        sowEndpointJsonString = getJsonString("/sow-endpoint.json");
    }

    @BeforeEach
    void setup() {
        this.gameId = "41a483c4-6220-424a-a931-d9114a4f6748";
        this.game = new GameModel("41a483c4-6220-424a-a931-d9114a4f6748", numberOfPits);
    }

    @Test
    @DisplayName(value = "Test when gameInstance endpoint is invoked")
    void testGameInstanceEndpoint() throws Exception {
        this.gameInstanceReqModel = new GameInstanceApiRequestModel(this.gameId);

        when(gameService.createInstance(this.gameId, this.numberOfPits)).thenReturn(this.game);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/game/instance")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(this.gameInstanceReqModel)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(gameInstanceJsonString)).andReturn();

    }

    @Test
    @DisplayName(value = "Test when sow endpoint is invoked")
    void testSowEndpoint() throws Exception {

        this.sowApiRequestModel = new SowApiRequestModel(this.gameId, 1);

        when(cache.get(this.gameId)).thenReturn(this.game);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/sow")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(this.sowApiRequestModel)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(sowEndpointJsonString)).andReturn();
    }

    private static String getJsonString(String path) throws IOException {
        File file = new ClassPathResource(path).getFile();
        return new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);
    }
}
