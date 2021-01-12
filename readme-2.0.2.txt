目录
1 版本说明
2 创建exotic-harpoon项目
    2.1 创建服务发现/注册中心eureka及高可用部署
    2.2 eureka集群高可用部署
    2.3 创建配置中心config项目
    2.4 创建微服务order,product,user项目
3 引入jpa数据源
4 配置中心读取文件方式
    4.1 git方式读取
    4.2 native方式读取
5 自动刷新配置文件
6 Feign接口及HystrixCommand熔断
7 添加SBA
8 Zuul网关配置
    8.1 服务网关的要素
    8.2 常用的网关方案
    8.3 Zuul特点
    8.4 路由
    8.5 过滤器
    8.6 Zuul的高可用
9 引入Zipkin
10 引入AMQP，默认实现为rabbitmq
11 校验重复提交

内容
1 版本说明
分支1.0.0使用版本
spring cloud版本为：Hoxton.SR9
spring boot版本为：2.3.5.RELEASE
spring boot admin版本为：2.3.1

2 创建exotic-harpoon项目
创建项目目录结构参考note/intellij-idea/创建父子结构.txt
2.1 创建服务发现/注册中心eureka
a) 引入依赖
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
</dependency>
b) 在启动类上添加@SpringBootApplication, @EnableEurekaServer注解
c) resources/bootstrap.yml配置文件
server:
  port: 8761
spring:
  application:
    name: eureka01
eureka:
  # eureka实例相关配置
  instance:
    # eureka实例主机名
    hostname: localhost
    # 表示在eureka server在接收到上一个心跳之后等待下一个心跳的秒数（默认90秒），若不能在指定时间内收到心跳，则移除此实例，并禁止此实例的流量
    # 必须高于 lease-renewal-interval-in-seconds
#    lease-expiration-duration-in-seconds: 90
    # 表示 Eureka Client 向 Eureka Server 发送心跳的频率（默认 30 秒），
    # 如果在 lease-expiration-duration-in-seconds 指定的时间内未收到心跳，则移除该实例。貌似在eureka server中配置没什么用处！
#    lease-renewal-interval-in-seconds: 20
  client:
    service-url:
      # 集群环境中，其它的eureka节点
      defaultZone: http://localhost:8762/eureka/,http://localhost:8763/eureka/
#      defaultZone: http://${eureka.instance.hostname}:${server.port}/eureka/
    # 指示此实例是否应将其信息注册到eureka服务器以供其他人发现
    # 如果是单个节点，应该设置为false，表示自己就是注册中心，不用注册自己。
    # 在集群环境中，应该设置为true，表示注册自己（defaultZone不用填自己地址，填写其它eureka服务地址）
    register-with-eureka: false
    # 表示是否此client从eureka server拉取eureka注册信息。
    # 如果是单个服务，应该设置为false，表示自己就是注册中心，不用去注册中心获取其他服务的地址。
    # 在集群环境中，应该设置为true，表示从eureka server中拉取其它服务信息。
    fetch-registry: false
  server:
    # 自我保护模式，默认打开（当出现网络分区导致丢失过多客户端时，不删除任何微服务）
    enable-self-preservation: false
    # eureka server清理无效节点的时间间隔，默认60000毫秒，即60秒
    eviction-interval-timer-in-ms: 10000
    # 禁用Eureka的ReadOnlyMap缓存，解决eureka 的双缓存问题
#    use-read-only-response-cache: false

2.2 eureka集群高可用部署
参考 fxcjj/eureka-ha 项目

2.3 创建配置中心config项目
a) 引入依赖
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-config-server</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>
b) 在启动类上添加注解
@SpringBootApplication
@EnableDiscoveryClient
@EnableConfigServer
c) resources/bootstrap.yml配置文件
server:
  port: 8091
spring:
  application:
    name: config
#  profiles:
#    active: dev
# 将服务注册到eureka
eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/
# 日志配置
logging:
  # 日志输出级别
  level:
    # 不输出 Resolving eureka endpoints via configuration
    com.netflix.discovery.shared.resolver.aws.ConfigClusterResolver: warn

2.4 创建微服务order,product,user项目
a) 创建order项目（product,user项目类似）
<!-- eureka client -->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>

<!-- web请求 -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>

<!-- lombok插件 -->
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
</dependency>

<!-- 此项目为config client，即通过config server获取项目配置文件 -->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-config-client</artifactId>
</dependency>

b) 在启动类上添加注解
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.vic.feign"})
@EntityScan("com.vic")
@ComponentScan(basePackages = {"com.vic"})

c) resources/bootstrap.yml配置文件
spring:
  application:
    name: order
  cloud:
    config:
      discovery:
        enabled: true
        service-id: CONFIG #config server id
      profile: dev # 详细说明参考user/bootstrap.yml

# 注册中心地址
eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/
    # 开启健康检查
    healthcheck:
      enabled: true
    # 设置实例是否注册到eureka server，默认为true
    register-with-eureka: true
  instance:
    # eureka client发送心跳给server端的频率。如果在leaseExpirationDurationInSeconds后，server端没有收到client的心跳，则将摘除该instance
    lease-renewal-interval-in-seconds: 30
    # 告知服务端N秒还未收到心跳的话，就将该服务移除列表
    # eureka server至上一次收到client的心跳之后，等待下一次心跳的超时时间，在这个时间内若没收到下一次心跳，则将移除该instance
    lease-expiration-duration-in-seconds: 90
# 日志配置
logging:
  # 日志输出级别
  level:
    # 不输出 Resolving eureka endpoints via configuration
    com.netflix.discovery.shared.resolver.aws.ConfigClusterResolver: warn

3 引入jpa数据源
a) 在order,product,user项目引入依赖
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>

<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
</dependency>

b) 在order,product,user项目bootstrap.yml中配置数据源
spring:
  # 数据源配置
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/springcloud_sell?serverTimezone=UTC&useUnicode=true&characterEncoding=utf8
    username: root
    password: root
  # JPA配置
  jpa:
    show-sql: true
    database-platform: org.hibernate.dialect.MySQLDialect

4 配置中心读取文件方式
4.1 git方式读取
a) 在git上新建一个项目用来存放配置文件
配置文件命名格式
/{applicationName}-{profiles}.yml
/{label}/{applicationName}-{profiles}.yml

applicationName 表示服务名
profiles 表示环境
label 表示分支

如：
localhost:8080/order-test.yml
localhost:8080/release/order-test.yml

basedir // 配置git拉下来的文件路径,注意安全!生产环境常用!
config可启动多个实例达到高可用,生产环境部署多个即可

// 注意，访问order-a.yml会合并到order.yml文件！！！！
order.yml
order-a.yml
order-b.yml

b) 调整config模块的bootstrap.yml配置
spring:
  application:
    name: config
  cloud:
    config:
      # Flag to say that remote configuration is enabled. Default true;
      enabled: true
      server:
        # git方式读取配置信息
        git:
#          basedir: D:\ideaprojectsa\git-config\basedir
          # 配置git仓库地址
          uri: https://gitee.com/fxcjj/config-repo
          username: 643117227@qq.com
          password: myiv901225
          # 指定分支
          default-label: 2.0.1
          # 指定搜索目录
          search-paths: config-files

c) 在各微服务引入依赖及相关配置
pom.xml文件配置
<!-- 此项目为config client，即通过config server获取项目配置文件 -->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-config-client</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>

bootstrap.yml文件配置
spring:
  cloud:
    config:
      discovery:
        enabled: true
        #config server id
        service-id: CONFIG
      profile: dev # 详细说明参考user/bootstrap.yml
注意：order先去eureka拿到config,再去拿到配置文件，如果在order的bootstrap.yml中如果未配置eureka地址，则默认找localhost:8761。

4.2 native方式读取
a) 在config模块的resource目录添加相关文件
# 基础配置
base
 - admin-dev.yml
# 公共配置
common
 - cache-dev.yml
 - common-config-dev.yml
# 网关配置
gate
 - api-gateway-dev.yml
# 模块配置
module
 - order-dev.yml
 - product-dev.yml
 - user-dev.yml

b) 修改config模块的bootstrap.yml文件
spring:
  application:
    name: config
  cloud:
    config:
      # Flag to say that remote configuration is enabled. Default true;
      enabled: true

      # 配置方式
      server:
        bootstrap: true

        # 方式一: git方式读取配置信息
#        git:
##          basedir: D:\ideaprojectsa\git-config\basedir
#          # 配置git仓库地址
#          uri: https://gitee.com/fxcjj/config-repo
#          username: 643117227@qq.com
#          password: myiv901225
#          default-label: 2.0.2
#          search-paths: /base,/common,/gate,/module
#          clone-on-start: true

        # 方式二: 本地方式读取配置信息
        native:
          search-locations: classpath:/base/,classpath:/common/,classpath:/gate/,classpath:/module/

  # 此项目的环境配置，native,dev,test/uat,prod
  profiles:
    active: ${config.active:native}

c) 测试
修改user-dev.yml文件，添加haha参数。
依次启动eureka,config,user,访问user模块的 localhost:18070/test/haha
注意：当修改了配置文件，需要重启config模块及相关模块

5 自动刷新配置文件
a) 在config模块pom.xml中配置
<!-- 添加消息总线支持 Spring Cloud Bus -->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-bus-amqp</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>

b) 配置config模块bootstrap.yml
spring:
  # 消息队列
  rabbitmq:
    host: 127.0.0.1
    port: 5672
    username: guest
    password: guest
    virtual-host: /

c) 配置各微服务pom.xml和bootstrap.yml
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

d) 测试
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

e) 配置webhooks自动刷新配置
在管理 -> WebHooks -> 添加
// 地址actuator/bus-refresh2重写了
Url: http://victor.free.idcfengye.com/actuator/bus-refresh2
勾选Push、激活，添加即可

// 配置webhooks时出现 through reference chain: java.util.LinkedHashMap["commits"]
参考: https://bbs.csdn.net/topics/392378475

两种解决方案处理
1) com.vic.config.controller.ActuatorController
2) com.vic.config.filter.WebHooksFilter

安装rabbitmq请参考 note/rabbitmq/install.txt

6 Feign接口及HystrixCommand熔断
参考类
com.vic.orderserver.controller.Client1Controller
com.vic.orderserver.controller.Client2Controller

参考项目
https://github.com/fxcjj/exotic-feign-ribbon-hystrix

7 添加SBA
admin 会自己拉取 Eureka 上注册的 app 信息，主动去注册。
这也是唯一区别之前入门中手动注册的地方，就是 client 端不需要 admin-client 的依赖，
也不需要配置 admin 地址了，一切全部由 admin-server 自己实现。
这样的设计对环境变化很友好，不用改了admin-server后去改所有app 的配置了。
a) 新建admin项目
b) 引入依赖
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>

<!-- 实现Admin Server的安全控制 -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>

<dependency>
    <groupId>de.codecentric</groupId>
    <artifactId>spring-boot-admin-starter-server</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>

c) 在启动类上添加注解
@EnableAdminServer
@SpringBootApplication
@EnableDiscoveryClient

d) resources/bootstrap.yml配置
server:
  port: 8097
spring:
  application:
    name: admin
  # admin登录
  security:
    user:
      name: admin
      password: 123456
  boot:
    admin:
      discovery:
        # 不显示admin的监控信息
        ignored-services: ${spring.application.name}

# 将服务注册到eureka
eureka:
  client:
    # 健康检查
    healthcheck: true
    service-url:
      defaultZone: http://localhost:8761/eureka/
    # 指示轮询eureka服务器信息更改的频率(以秒为单位)。Eureka服务器可以添加或删除，这个设置控制了Eureka客户端何时应该知道它。
    # 也可以使用变量名 eurekaServiceUrlPollIntervalSeconds
    eureka-service-url-poll-interval-seconds: 300
  instance:
    # 该实例相关的元数据信息，会一同注册到eureka server，供其它实例使用
    metadata-map:
      user.name: ${spring.security.user.name}
      user.password: ${spring.security.user.password}
    # # 表示 Eureka Client 向 Eureka Server 发送心跳的频率（默认 30 秒），如果在 lease-expiration-duration-in-seconds 指定的时间内未收到心跳，则移除该实例。
    lease-renewal-interval-in-seconds: 20
# 端点管理
management:
  endpoints:
    web:
      exposure:
        # 暴露所有端点
        include: "*"
  endpoint:
    # health端点显示详细信息
    health:
      show-details: always
# 日志配置
logging:
  # 日志输出级别
  level:
    # 不输出 Resolving eureka endpoints via configuration
    com.netflix.discovery.shared.resolver.aws.ConfigClusterResolver: warn

e) 各项目需要引入依赖及开放端点
pom.xml配置
<!-- actuator -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>

bootstrap.yml配置
# endpoint
management:
  endpoints:
    web:
      exposure:
        # 暴露所有端点
        include: "*"
  endpoint:
    health:
      # 显示健康具体信息，默认不会显示详细信息
      show-details: always

f) 登录配置及访问地址
//安全配置
com.vic.admin.config.SecuritySecureConfig
输入地址: http://localhost:8097
输入用户名/密码: admin/123456

g) 在SBA中调整日志级别
例如：查询SQL输出
在“日志配置”菜单中，搜索 org.hibernate.SQL，设置日志级别为DEBUG。
观察日志即可看到sql输出
测试方法在 com.vic.userserver.controller.TestController.queryUserById

8 Zuul网关配置
a) 引入依赖
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-zuul</artifactId>
</dependency>

b) 在启动类上添加注解
@EnableZuulProxy

8.1 服务网关的要素
a) 稳定性、高可用
b) 性能、并发性
c) 安全性
d) 扩展性

8.2 常用的网关方案
a) Nginx+Lua
性能和稳定性不错
b) Kong
要花钱的
c) Tyk
轻量级，go语言
d) Spring Cloud Zuul

8.3 Zuul特点
a) 路由+过滤器=zuul
b) 一系列过滤器

8.4 路由
a) 依次启动eureka, config, api-gateway, order, product模块
由于 Spring Cloud Zuul 在整合了 Eureka 之后，具备默认的服务路由功能，
即：当我们这里构建的api-gateway应用启动并注册到 Eureka 之后，服务网关会发现上面我们启动的两个服务order和product，
这时候 Zuul 就会创建两个路由规则。
每个路由规则都包含两部分，一部分是外部请求的匹配规则，另一部分是路由的服务 ID。
针对当前示例的情况，Zuul 会创建下面的两个路由规则：
   * 转发到producer服务的请求规则为：/order/**
   * 转发到consumer服务的请求规则为：/product/**

b) 测试1
// 测试网关
http://localhost:9000/env/print
// 通过网关访问order模块
http://localhost:9000/order/test/a
// 通过网关访问product模块
http://localhost:9000/product/test/a

c) 测试2
api-gateway配置如下：
# feign相关配置，如果未配置default，connectTimeout默认10s，readTimeout默认60s，参考类 feign.Request.Options
# 默认关闭Feign的重试机制
feign:
  client:
    config:
      # feign默认配置，可指定服务名声明相关feign配置
      default:
        # feign接口默认连接超时配置
        connectTimeout: 10000
        # feign接口默认读取超时配置
        readTimeout: 60000
        loggerLevel: full
      # 指定服务的feign配置
      producer1:
        connectTimeout: 50000
        readTimeout: 80000
        loggerLevel: full
  hystrix:
    # hystrix断路器开关
    # If true, an OpenFeign client will be wrapped with a Hystrix circuit breaker
    enabled: true
  # feign压缩
  compression:
    # feign请求压缩相关
    request:
      # 压缩开关
      enabled: true
      # The list of supported mime types.
      mime-types: text/html,application/xml,application/json
      # The minimum threshold content size.
      min-request-size: 2048
    # feign响应压缩相关
    response:
      # 压缩开关
      enabled: true

# feign的负载均衡ribbon配置，spring cloud feign 默认开启支持ribbon，开关在spring.cloud.loadbalancer.ribbon.enabled配置
# ribbon的全局配置
ribbon:
  # ribbon请求连接实例的超时时间，默认值2000
  ConnectTimeout: 10000
  # 负载均衡超时处理时间，默认值5000
  ReadTimeout: 30000
  # 同一台实例最大重试次数,不包括首次调用，默认0
  MaxAutoRetries: 0
  # 重试负载均衡其他的实例最大重试次数,不包括首次调用，默认1
  MaxAutoRetriesNextServer: 1
  # 是否所有操作都重试
  OkToRetryOnAllOperations: false

# ribbon的局部配置
# 略

# hystrix.timeoutInMilliseconds   >=  ribbonTimeout = (ribbonReadTimeout + ribbonConnectTimeout) * (maxAutoRetries + 1) * (maxAutoRetriesNextServer + 1)
# hystrix的超时时间，开关在feign.hystrix.enabled
hystrix:
  command:
    # default全局有效，service id指定应用有效
    default:
      execution:
        timeout:
          # 如果enabled设置为false，则请求超时交给ribbon控制,为true,则超时作为熔断根据
          enabled: true
        isolation:
          thread:
            # 断路器超时时间，默认1000ms
            timeoutInMilliseconds: 50000

order模块测试类

@RestController
@Slf4j
public class Client2Controller {
    @Autowired
    ProductClient productClient;

    @GetMapping("getProductList")
    public String getProductList() {
        log.info("invoke getProductList method start");
        List<ProductInfoOutput> productInfos = productClient.listForOrder(Arrays.asList("164103465734242707"));
        log.info("invoke getProductList method end, result:{}", JSON.toJSONString(productInfos));
        return "ok";
    }
}

product模块测试类
@PostMapping("listForOrder")
public List<ProductInfo> listForOrder(@RequestBody List<String> productIdList) {
    log.info("invoke listForOrder method start");
    try {
        Thread.sleep(5000);
    } catch (InterruptedException e) {
        e.printStackTrace();
    }
    List<ProductInfo> list = productService.findList(productIdList);
    log.info("invoke listForOrder method result:{}", JSON.toJSONString(list));
    return list;
}

当通过网关访问时，因为不满足
hystrix.timeoutInMilliseconds   >=  ribbonTimeout = (ribbonReadTimeout + ribbonConnectTimeout) * (maxAutoRetries + 1) * (maxAutoRetriesNextServer + 1)
所以console会打印如下信息：
2020-12-23 14:52:52 - [api-gateway,,,] - The Hystrix timeout of 50000ms for the command order is set lower than the combination of the Ribbon read and connect timeout, 80000ms.

8.5 过滤器
过滤器生命周期：
a) pre filters
b) routing filter(s)
c) post filter(s) //已经拿到结果，可以对结果的处理或加工
d) error filter //上面三个发生异常时走过个
e) custom filter //自定义filter，可以放在prefilter和postfilter
f) 前置Pre可以做：
	* 限流
	* 鉴权
	* 参数检验
g) 后置Post可以做：
	* 统计
	* 日志

8.6 Zuul的高可用
    * 多个Zuul节点注册到Eureka Server
    * Nginx和Zuul混搭

为api-gateway添加lb-api-gateway，所有请求通过lb-api-gateway请求转发

9 引入Zipkin
a) 以order模块为例
b) 引入依赖
<!-- 包含了starter-sleuth和sleuth-zipkin -->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-zipkin</artifactId>
</dependency>

c) 在bootstrap.yml中配置
spring:
  # zipkin链路追踪
  zipkin:
    base-url: http://192.168.6.131:9411
    # Enables sending spans to zipkin
    enabled: false
  sleuth:
    sampler:
      # 默认只将10%的请求发出去
      probability: 1.0
    web:
      client:
        enabled: true

d) docker搭建zipkin
# 拉取最新版本zipkin镜像
docker pull openzipkin/zipkin:latest
# 启动镜像
docker run --name my-zipkin -d -p 9411:9411 openzipkin/zipkin

e) 相关说明
核心步骤
    数据采集
    数据存储
    查询展示

OpenTracing是一种标准
优势
CNCF

10 引入AMQP，默认实现为rabbitmq

11 校验重复提交



Reference
https://blog.csdn.net/ThinkWon/article/details/103786588