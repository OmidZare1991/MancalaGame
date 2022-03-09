package bol.com.mankala;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ConfigFile {
    @Value("${app.cache.update.time.millis}")
    private Long cacheUpdateTime;

    public Long getCacheUpdateTime() {
        return cacheUpdateTime;
    }

    public void setCacheUpdateTime(Long cacheUpdateTime) {
        this.cacheUpdateTime = cacheUpdateTime;
    }
}
