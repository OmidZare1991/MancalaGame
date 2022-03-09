package bol.com.mancala;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ConfigFile {
    @Value("${mancala.game.cache.update.time.millis}")
    private Long cacheUpdateTime;
    @Value("${mancala.game.pit.stones}")
    private Integer pitStones;


    public Integer getPitStones() {
        return pitStones;
    }

    public void setPitStones(Integer pitStones) {
        this.pitStones = pitStones;
    }

    public Long getCacheUpdateTime() {
        return cacheUpdateTime;
    }

    public void setCacheUpdateTime(Long cacheUpdateTime) {
        this.cacheUpdateTime = cacheUpdateTime;
    }
}
