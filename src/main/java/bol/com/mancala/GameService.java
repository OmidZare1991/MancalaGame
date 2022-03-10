package bol.com.mancala;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class GameService {
    private static final Logger LOGGER = LoggerFactory.getLogger(GameService.class);

    public MancalaGame createInstance(String id, Integer stones) {
        LOGGER.debug("Game instance with id {} and stones {} created", id, stones);
        return new MancalaGame(id, stones);
    }
}
