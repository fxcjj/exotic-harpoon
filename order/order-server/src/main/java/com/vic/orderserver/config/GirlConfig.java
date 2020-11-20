package com.vic.orderserver.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Slf4j
@Data
@Component
@ConfigurationProperties("girl")
//@RefreshScope
public class GirlConfig {
    private String name;
    private Integer age;

    /**
     * init GirlConfig: GirlConfig(name=lili, age=19)
     */
    @PostConstruct
    public void init() {
        log.info("init GirlConfig: {}", this);
    }
}
