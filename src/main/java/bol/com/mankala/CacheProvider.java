package bol.com.mankala;

import com.github.benmanes.caffeine.cache.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CacheProvider {
    @Autowired
    private Cache<String, MancalaGame> cache;

    public void updateCache(MancalaGame game) {
        cache.put(game.getId(), game);
    }

    public MancalaGame loadFromCache(String id) {
        return cache.getIfPresent(id);
    }

}
