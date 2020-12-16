package com.vic.config.filter;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * /actuator/bus-refresh 过滤器,清空/actuator/bus-refresh请求的Request body
 *
 * 解决Gitlab webhooks触发配置中心/actuator/bus-refresh请求返回400错误码的问题
 *
 * config模块日志打印如下：
 * 2020-12-16 18:17:15.887  WARN 14980 --- [nio-8091-exec-8] .w.s.m.s.DefaultHandlerExceptionResolver :
 * Resolved [org.springframework.http.converter.HttpMessageNotReadableException: JSON parse error:
 * Cannot deserialize instance of `java.lang.String` out of START_ARRAY token;
 * nested exception is com.fasterxml.jackson.databind.exc.MismatchedInputException:
 * Cannot deserialize instance of `java.lang.String` out of START_ARRAY token
 *  at [Source: (PushbackInputStream); line: 1, column: 315] (through reference chain: java.util.LinkedHashMap["commits"])]
 */
//@Component
public class WebHooksFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String url = httpServletRequest.getRequestURI();
        System.out.println("httpServletRequest.getRequestURI()=======>"+url);
        if(!url.endsWith("/bus-refresh")){
            filterChain.doFilter(servletRequest,servletResponse);
            return;
        }
        RequestWrapper requestWrapper = new RequestWrapper(httpServletRequest);
        filterChain.doFilter(requestWrapper, servletResponse);
    }
  
    private class RequestWrapper extends HttpServletRequestWrapper {
        public RequestWrapper(HttpServletRequest request) {
            super(request);
        }
  
        @Override
        public ServletInputStream getInputStream() throws IOException {
            byte[] bytes = new byte[0];
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
            ServletInputStream servletInputStream = new ServletInputStream() {
                @Override
                public int read() throws IOException {
                    return byteArrayInputStream.read();
                }
  
                @Override
                public boolean isFinished() {
                    return byteArrayInputStream.read() == -1 ? true : false;
                }
  
                @Override
                public boolean isReady() {
                    return false;
                }
  
                @Override
                public void setReadListener(ReadListener listener) {
  
                }
            };
            return servletInputStream;
        }
    }
}