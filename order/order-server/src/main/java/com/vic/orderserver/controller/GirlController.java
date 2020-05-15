package com.vic.orderserver.controller;

import com.vic.orderserver.config.GirlConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 1. 第一种服务之间通信方式RestTemplate, @LoadBalanced
 */
@RestController
@Slf4j
@RequestMapping("girls")
public class GirlController {

    @Autowired
    private GirlConfig girlConfig;

    @GetMapping("print")
    public String print() {
        return "name: " + girlConfig.getName() + ", age: " + girlConfig.getAge();
    }


}