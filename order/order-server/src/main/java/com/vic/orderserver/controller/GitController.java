package com.vic.orderserver.controller;

import com.vic.orderserver.config.GirlConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试是否取到git上的配置文件
 */
@RestController
@Slf4j
@RequestMapping("git")
@RefreshScope
public class GitController {

    @Autowired
    private GirlConfig girlConfig;

    @GetMapping("config")
    public GirlConfig config() {
        return girlConfig;
    }

}