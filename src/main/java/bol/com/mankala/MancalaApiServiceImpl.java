package bol.com.mankala;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MancalaApiServiceImpl implements MancalaApi {
    private static final Logger LOGGER = LoggerFactory.getLogger(MancalaApiServiceImpl.class);
    @Autowired
    private CacheProvider cacheProvider;

    @Override
    public MancalaGame createGame(int stones, String id) {
        return new MancalaGame(id, stones);
    }

    public MancalaGame loadMancalaGame(String id) {
        MancalaGame mancalaGame = cacheProvider.loadFromCache(id);
        if (null == mancalaGame) {
            LOGGER.error("game with id {} not found", id);
            throw new MancalaException("game with id: " + id + " not found");
        }
        LOGGER.debug("game {} with id {} loaded from cache", mancalaGame, id);
        return mancalaGame;
    }

    public void updateMancalaGame(MancalaGame game) {
        cacheProvider.updateCache(game);
    }
}
