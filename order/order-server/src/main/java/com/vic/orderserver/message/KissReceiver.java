package com.vic.orderserver.message;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@EnableBinding(Processor.class) //使用stream自带的Processor
public class KissReceiver {

    @StreamListener(Processor.INPUT) //监听消息
    @SendTo(Processor.OUTPUT) //返回应答
    public String receive(Object message) {
        log.info("KissService received: " + message);
        return "i am happy";
    }

    @StreamListener(Processor.OUTPUT) //监听消息
    public void receive1(Object message) {
        log.info("receive1 KissService received: " + message);
    }



}
