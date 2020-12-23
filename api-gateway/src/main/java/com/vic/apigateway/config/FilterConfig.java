package com.vic.apigateway.config;

import com.vic.apigateway.filter.AddResponseHeaderFilter;
import com.vic.apigateway.filter.TokenFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * filter配置
 */
//@Configuration
public class FilterConfig {

    @Bean
    public TokenFilter tokenFilter() {
        return new TokenFilter();
    }

    @Bean
    public AddResponseHeaderFilter addResponseHeaderFilter() {
        return new AddResponseHeaderFilter();
    }
}
