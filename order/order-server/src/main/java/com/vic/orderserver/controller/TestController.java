package com.vic.orderserver.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author victor
 * date: 2020/12/22 17:54
 */
@Slf4j
@RestController
@RequestMapping("test")
public class TestController {

    @GetMapping("a")
    public String a() {
        return "ok";
    }

}
