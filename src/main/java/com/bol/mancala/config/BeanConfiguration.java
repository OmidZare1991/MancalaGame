package com.bol.mancala.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
@EnableSwagger2
public class BeanConfiguration {

    @Bean
    public Cache caffeineConfig(ApplicationConfig config) {
        return
                Caffeine.newBuilder()
                        .expireAfterWrite(config.getCacheUpdateTime(), TimeUnit.MINUTES)
                        .build();
    }

    @Bean
    public Docket apiDocket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select() // to get the docket builder (ApiSelectorBuilder)
                .apis(RequestHandlerSelectors.basePackage("com.bol.mancala"))
                .paths(PathSelectors.ant("/api/**"))
                .build()
                .apiInfo(getApiInfo());
    }

    private ApiInfo getApiInfo() {
        return new ApiInfo(
                "Manacala Game APIs",
                "This application provides APIs for creating Mancala game application",
                "1.0.0",
                null,
                new Contact("Omid", "http://linkedin.com/in/omid-zare-017095b2", "o.zare70@gmail.com"),
                null,
                null,
                Collections.emptyList()
        );
    }

}
