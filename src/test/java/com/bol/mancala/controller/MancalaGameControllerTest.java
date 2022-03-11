//package com.bol.mancala.controller;
//
//import com.bol.mancala.model.dto.SowDto;
//import com.bol.mancala.repository.MancalaMancalaGameRepositoryImpl;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//
//import java.io.File;
//import java.io.IOException;
//import java.nio.charset.StandardCharsets;
//import java.nio.file.Files;
//
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@SpringBootTest()
//@ExtendWith(MockitoExtension.class)
//@AutoConfigureMockMvc
//public class MancalaGameControllerTest {
//
//    private static String gameInstanceJsonString;
//    private static String sowEndpointJsonString;
//
//    @MockBean
//    private MancalaMancalaGameRepositoryImpl cache;
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//
//    @BeforeAll
//    static void init() throws IOException {
//        gameInstanceJsonString = getJsonString("/src/test/controller/game-instance-endpoint.json");
//        sowEndpointJsonString = getJsonString("/src/test/controller/sow-endpoint.json");
//    }
//
//    @Test
//    @DisplayName(value = "Test when gameInstance endpoint is invoked")
//    void testGameInstanceEndpoint() throws Exception {
//
//        mockMvc.perform(MockMvcRequestBuilders.post("/api/game/instance")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(this.objectMapper.writeValueAsString(gameInstanceReqModel)))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(content().json(gameInstanceJsonString)).andReturn();
//
//    }
//
//    @Test
//    @DisplayName(value = "Test when sow endpoint is invoked")
//    void testSowEndpoint() throws Exception {
//
//        SowDto sowDto = new SowDto(this.gameId, 1);
//
//        mockMvc.perform(MockMvcRequestBuilders.post("/api/sow")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(this.objectMapper.writeValueAsString(sowDto)))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(content().json(sowEndpointJsonString)).andReturn();
//    }
//
//    private static String getJsonString(String path) throws IOException {
//        File file = new ClassPathResource(path).getFile();
//        return new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);
//    }
//}
