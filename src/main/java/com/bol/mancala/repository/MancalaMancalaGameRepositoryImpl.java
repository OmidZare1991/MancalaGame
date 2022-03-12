package com.bol.mancala.repository;

import com.bol.mancala.model.MancalaGame;
import com.github.benmanes.caffeine.cache.Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MancalaMancalaGameRepositoryImpl implements MancalaGameRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(MancalaMancalaGameRepositoryImpl.class);
    @Autowired
    private Cache<String, MancalaGame> cache;

    @Override
    public void update(MancalaGame game) {
        cache.put(game.getId(), game);
    }

    @Override
    public MancalaGame get(String id) {
        MancalaGame game = cache.getIfPresent(id);
        LOGGER.info("game {} with id {} loaded from cache", game, id);
        return game;
    }

}
