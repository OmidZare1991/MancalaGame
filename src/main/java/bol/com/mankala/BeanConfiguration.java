package bol.com.mankala;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class BeanConfiguration {

    @Bean
    public Cache caffeineConfig(ConfigFile config) {
        return
                Caffeine.newBuilder()
                        .expireAfterWrite(config.getCacheUpdateTime(), TimeUnit.MINUTES)
                        .build();
    }
}
