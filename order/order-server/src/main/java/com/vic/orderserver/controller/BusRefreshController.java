package com.vic.orderserver.controller;

import com.vic.config.BusRefreshConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试是否取到git上的配置文件
 */
@RestController
@Slf4j
@RequestMapping("busRefresh")
//@RefreshScope
public class BusRefreshController {

    @Autowired
    private BusRefreshConfig config;

    @GetMapping("config")
    public BusRefreshConfig config() {
        return config;
    }

}