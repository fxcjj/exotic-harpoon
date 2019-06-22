package com.vic.apigateway.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ZuulConfig {

    /**
     * 动态加载配置
     * 如果不想写此类，可以写在ApiGatewayApplication里面
     * @return
     */
    @ConfigurationProperties("zuul")
    @RefreshScope
    public ZuulProperties zuulProperties() {
        return new ZuulProperties();
    }

}
