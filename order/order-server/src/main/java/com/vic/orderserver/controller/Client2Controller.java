package com.vic.orderserver.controller;

import com.vic.bo.product.DecreaseStockInput;
import com.vic.bo.product.ProductInfoOutput;
import com.vic.feign.product.ProductClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

/**
 * 1. 第二种服务之间通信方式，Feign
 */
@RestController
@Slf4j
public class Client2Controller {

    @Autowired
    ProductClient productClient;

    @GetMapping("/getProductList")
    public String getProductList() {

        List<ProductInfoOutput> productInfos = productClient.listForOrder(Arrays.asList("164103465734242707"));
        log.info("response: {}", productInfos);

        return "ok";
    }

    @GetMapping("/decreaseStock")
    public String decreaseStock() {

        productClient.decreaseStock(Arrays.asList(new DecreaseStockInput("164103465734242707", 2)));

        return "ok";
    }


}