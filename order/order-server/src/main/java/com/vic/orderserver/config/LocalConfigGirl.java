package com.vic.orderserver.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Slf4j
@Data
@Component
@ConfigurationProperties("local.config.girl")
//@RefreshScope
public class LocalConfigGirl {
    private String name;
    private Integer age;

    /**
     * init LocalConfigGirl: LocalConfigGirl(name=justin, age=29)
     */
    @PostConstruct
    public void init() {
        log.info("init LocalConfigGirl: {}", this);
    }
}
