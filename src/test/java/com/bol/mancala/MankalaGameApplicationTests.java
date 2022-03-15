package com.bol.mancala;

import com.bol.mancala.service.MancalaGameBuilder;
import com.bol.mancala.service.MancalaGameService;
import com.github.benmanes.caffeine.cache.Cache;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class MankalaGameApplicationTests {
    @Autowired
    private MancalaGameBuilder gameBuilder;
    @Autowired
    private MancalaGameService service;

    @Autowired
    private Cache<String, String> cache;
    @Test
    void contextLoads() {

    }

    @Test
    void test() {

//        MancalaGame game = gameBuilder.getNew(0);
//        game.getPit(7).setStones(15);
//        game.getPit(14).setStones(2);
//
//        for (int i = 8; i <= 14; i++) {
//            game.getPit(i).setStones(2);
//        }
//
//        MancalaGame gameFinished = service.getFinalGameState(game);
//        System.out.println(gameFinished.getPit(7));
//        System.out.println(gameFinished.getPit(14));
//        System.out.println(gameFinished.toString());

//        String omid = cache.getIfPresent("omid");
//        assertNull(omid);
//        String omid = cache.get("omid", k -> "zare");
//        assertEquals("zare",omid);

        String omid2 = cache.get("omid", k -> k + "zare");
        System.out.println(omid2);
        assertEquals("omidzare", omid2);
    }
}
