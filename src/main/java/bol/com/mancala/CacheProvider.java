package bol.com.mancala;

import com.github.benmanes.caffeine.cache.Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CacheProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(CacheProvider.class);
    @Autowired
    private Cache<String, GameModel> cache;

    public void update(GameModel game) {
        cache.put(game.getGameId(), game);
    }

    public GameModel get(String id) {
        GameModel game = cache.getIfPresent(id);
        if (null == game) {
            LOGGER.error("game with id {} not found", id);
            throw new ResourceNotFoundException("game with id: " + id + " not found");
        }
        LOGGER.debug("game {} with id {} loaded from cache", game, id);
        return game;
    }

}
