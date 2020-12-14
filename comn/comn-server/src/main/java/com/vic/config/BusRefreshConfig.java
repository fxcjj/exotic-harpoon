package com.vic.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Slf4j
@Data
@Component
@ConfigurationProperties("bus-refresh")
@RefreshScope
public class BusRefreshConfig {

    private String name;

    @PostConstruct
    public void init() {
        log.info("init BusRefreshConfig: {}", this);
    }
}
