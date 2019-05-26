package com.vic.orderserver.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@Slf4j
public class KissController {

    @Autowired
    private Processor pipe;

    @GetMapping("/sendMessage")
    public void process() {
        String msg = "hi, honey, " + new Date();
        pipe.input().send(MessageBuilder.withPayload(msg).build());
        log.info("aaaaaaaaaaaaaaaaaaaa");
    }
}