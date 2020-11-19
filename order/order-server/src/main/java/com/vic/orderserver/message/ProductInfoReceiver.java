package com.vic.orderserver.message;

import com.alibaba.fastjson.JSON;
import com.vic.bo.product.ProductInfoOutput;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class ProductInfoReceiver {

    @RabbitListener(queuesToDeclare = @Queue("productInfo"))
    public void process(String message) {
        List<ProductInfoOutput> list = JSON.parseArray(message, ProductInfoOutput.class);
        log.info("从队列[{}]接收消息:{}", "productInfo", list);

        //存到redis中去
        //redis.set(String.format("product_stock_%s", productId), stock)

        log.info("将数据存放到redis中");
    }

}
