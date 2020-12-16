
1. 分布式配置中心组件


2. 动态拉取配置
2.1 配置config模块pom.xml
<!-- 添加消息总线支持 Spring Cloud Bus -->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-bus-amqp</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>

2.2 配置config模块bootstrap.yml
spring:
  # 消息队列
  rabbitmq:
    host: 127.0.0.1
    port: 5672
    username: guest
    password: guest
    virtual-host: /

2.3 配置各微服务pom.xml和bootstrap.yml
pom.xml文件添加：
<!-- 添加消息总线支持 Spring Cloud Bus -->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-bus-amqp</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>

bootstrap.yml文件添加：
spring:
  # 消息队列
  rabbitmq:
    host: 127.0.0.1
    port: 5672
    username: guest
    password: guest
    virtual-host: /

2.4 测试
依次启动eureka,config,order,product,user

// 从config模块读取配置文件
http://localhost:8091/2.0.1/order-dev.yml

// 从order模块读取配置文件
http://localhost:8090/busRefresh/config

修改order-dev.yml文件，从config模块读取文件时可以看到变化。
但从order模块读取配置文件没有变化。

从config模块调用接口
# 刷新所有配置
http://localhost:8091/actuator/bus-refresh
再次从order模块读取配置文件则可以看到变化。

# 刷新3355端口配置
http://localhost:8091/actuator/bus-refresh/config-client:3355

注意：使用/actuator/refresh不可以！

2.5 配置webhooks自动刷新配置
在管理 -> WebHooks -> 添加
// 地址actuator/bus-refresh2重写了
Url: http://victor.free.idcfengye.com/actuator/bus-refresh2
勾选Push、激活，添加即可

// 配置webhooks时出现 through reference chain: java.util.LinkedHashMap["commits"]
https://bbs.csdn.net/topics/392378475

两种解决方案处理
a) com.vic.config.controller.ActuatorController
b) com.vic.config.filter.WebHooksFilter



https://docs.spring.io/spring-cloud-stream-binder-rabbit/docs/3.0.10.RELEASE/reference/html/spring-cloud-stream-binder-rabbit.html



