package com.bol.mancala.repository;

import com.bol.mancala.model.MancalaGame;
import com.bol.mancala.service.MancalaGameBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class MancalaGameRepositoryImplTest {
    @Autowired
    private MancalaGameRepository gameRepository;
    @Autowired
    private MancalaGameBuilder gameBuilder;

    private MancalaGame game;


    @BeforeEach
    void setup() {
        Integer pitStonesCount = 6;
        this.game = this.gameBuilder.getNew(pitStonesCount);
        this.gameRepository.update(this.game);
    }

    @Test
    @DisplayName(value = "Test when retrieving element from repository which previously was updated in repository")
    void testRepository() {
        assertNotNull(this.gameRepository.get(this.game.getId()));
        assertEquals(this.game, this.gameRepository.get(this.game.getId()));
    }
}
