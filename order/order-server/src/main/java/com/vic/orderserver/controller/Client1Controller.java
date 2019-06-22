package com.vic.orderserver.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * 1. 第一种服务之间通信方式RestTemplate, @LoadBalanced
 */
@RestController
@Slf4j
public class Client1Controller {

    @Autowired
    LoadBalancerClient loadBalancerClient;

//    @Autowired
//    RestTemplate restTemplate;

    @GetMapping("/getProductMsg")
    public String msg() {
        //1. 第一种方式（使用RestTemplate）
//        RestTemplate restTemplate = new RestTemplate();
//        String response = restTemplate.getForObject("http://localhost:8080/msg", String.class);

        //2. 第二种方式（使用LoadBalancerClient和RestTemplate获取url）
        ServiceInstance serviceInstance = loadBalancerClient.choose("PRODUCT");
        String url = String.format("http://%s:%s", serviceInstance.getHost(), serviceInstance.getPort()) + "/msg";
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(url, String.class);

        //3. 第三种方式（在RestTemplateConfig中配置@LoadBalanced）
//        String response = restTemplate.getForObject("http://PRODUCT/msg", String.class);


        return response;
    }


}