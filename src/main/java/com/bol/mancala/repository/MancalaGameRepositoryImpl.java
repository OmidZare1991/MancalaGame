package com.bol.mancala.repository;

import com.bol.mancala.common.ErrorConstants;
import com.bol.mancala.exception.ResourceNotFoundException;
import com.bol.mancala.model.MancalaGame;
import com.github.benmanes.caffeine.cache.Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class MancalaGameRepositoryImpl implements MancalaGameRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(MancalaGameRepositoryImpl.class);
    @Autowired
    private Cache<String, MancalaGame> cache;

    /**
     * @param game the game object which is kept in cache repository
     */
    @Override
    public void update(MancalaGame game) {
        cache.put(game.getId(), game);
    }

    /**
     * @param id the game id
     * @return the objcet of game
     */
    @Override
    public MancalaGame get(String id) {
        MancalaGame game = cache.getIfPresent(id);

        if (null == game) {
            LOGGER.error("game with id {} not found", id);
            throw new ResourceNotFoundException(String.format(ErrorConstants.RESOURCE_NOT_FOUND_EXCEPTION_MESSAGE, id));
        }
        return game;
    }

}
