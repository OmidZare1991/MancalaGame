package bol.com.mancala.repository;

import bol.com.mancala.CacheProvider;
import bol.com.mancala.MancalaException;
import bol.com.mancala.MancalaGame;
import bol.com.mancala.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

@SpringBootTest
public class CacheProviderTest {
    @Autowired
    private CacheProvider cacheProvider;
    private String id;
    private String id2;
    private MancalaGame game;


    @BeforeEach
    void setup() {
        this.id = UUID.randomUUID().toString();
        this.id2 = UUID.randomUUID().toString();
        this.game = new MancalaGame(this.id, 6);

        cacheProvider.update(this.game);
    }

    @Test
    void testCacheProvider() {
        assertEquals(this.game, cacheProvider.get(this.id));
    }

    @Test
    void testCacheProviderWithException() {
        assertThrowsExactly(ResourceNotFoundException.class, () -> cacheProvider.get(this.id2));
    }

    // test is expected to be failed in this case
    @Test
    void testCacheProviderWithWrongException() {
        assertThrowsExactly(MancalaException.class, () -> this.cacheProvider.get(this.id2));
    }

}
