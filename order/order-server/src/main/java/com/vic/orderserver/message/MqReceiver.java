package com.vic.orderserver.message;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 接收mq消息
 */
@Slf4j
@Component
public class MqReceiver {

    //1. 队列要提前新建好
//    @RabbitListener(queues = "myQueue")
    //2. 自动创建队列
//    @RabbitListener(queuesToDeclare = @Queue("myQueue"))
    //3. 自动创建队列，绑定关系
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue("myQueue"),
            exchange = @Exchange("myExchange")
    ))
    public void process(String msg) {
        log.info("MqReceiver: {}", msg);
    }

    /**
     * 数码服务商服务
     * @param msg
     */
    @RabbitListener(bindings = @QueueBinding(
            exchange = @Exchange("myOrder"),
            key = "computer",
            value = @Queue("computerOrder")
    ))
    public void processComputer(String msg) {
        log.info("computer MqReceiver: {}", msg);
    }

    /**
     * 水果服务商服务
     * @param msg
     */
    @RabbitListener(bindings = @QueueBinding(
            exchange = @Exchange("myOrder"),
            key = "fruit",
            value = @Queue("fruitOrder")
    ))
    public void processFruit(String msg) {
        log.info("fruit MqReceiver: {}", msg);
    }
}
