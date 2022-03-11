//package com.bol.mancala.repository;
//
//import com.bol.mancala.model.MancalaGame;
//import com.bol.mancala.exception.ResourceNotFoundException;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.UUID;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
//
//@SpringBootTest
//public class MancalaGameRepositoryImplTest {
//    @Autowired
//    private MancalaMancalaGameRepositoryImpl mancalaGameRepositoryImpl;
//    private String id;
//    private String id2;
//    private MancalaGame game;
//
//
//    @BeforeEach
//    void setup() {
//        this.id = UUID.randomUUID().toString();
//        this.id2 = UUID.randomUUID().toString();
//        this.game = new MancalaGame(this.id, 6);
//
//        mancalaGameRepositoryImpl.update(this.game);
//    }
//
//    @Test
//    @DisplayName(value = "Test if cache manager works correctly")
//    void testCacheProvider() {
//        assertEquals(this.game, mancalaGameRepositoryImpl.get(this.id));
//    }
//
//    @Test
//    @DisplayName(value ="Test when cache does not have the resource. In this case ResourceNotFoundException is thrown.")
//    void testCacheProviderWithException() {
//        assertThrowsExactly(ResourceNotFoundException.class, () -> mancalaGameRepositoryImpl.get(this.id2));
//    }
//
//}
