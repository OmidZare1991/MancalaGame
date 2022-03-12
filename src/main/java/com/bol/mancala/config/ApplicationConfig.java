package com.bol.mancala.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ApplicationConfig {
    @Value("${mancala.game.cache.update.time.minutes}")
    private Long cacheUpdateTime;

    public Long getCacheUpdateTime() {
        return cacheUpdateTime;
    }

    public void setCacheUpdateTime(Long cacheUpdateTime) {
        this.cacheUpdateTime = cacheUpdateTime;
    }
}
