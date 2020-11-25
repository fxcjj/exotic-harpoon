package com.vic.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 罗利华
 * date: 2020/11/23 13:55
 */
@Slf4j
@RestController
@RequestMapping("test")
public class TestController {

    @Value("${haha}")
    private String haha;

    @RequestMapping("test1")
    public String test1() {
        for(int i = 0; i < 100; i++) {
            log.info("print log {}", i);
        }
        return "ok" + haha;
    }

}
