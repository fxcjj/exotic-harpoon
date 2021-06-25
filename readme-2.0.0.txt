1 版本说明
spring cloud版本为：Hoxton.SR9
spring boot版本为：2.3.5.RELEASE
spring boot admin版本为：2.3.1

2 项目简介
admin 服务监控
api-gateway 基于zuul组件的网关
config 配置中心
eureka 服务注册/发现中心
note 相关说明
order 订单模块
product 产品模块
user 用户模块
sql 项目相关脚本

3 项目架构关系
admin监控微服务
eureka为微服务注册中心
config为配置中心统一管理配置
order,product,user为微服务
api-gateway网关

4 相关技术点
非继承方式引入spring boot,spring cloud依赖
HystrixCommand熔断
Feign接口调用
引入AMQP，默认实现为rabbitmq
Zuul网关配置
引入Zipkin
校验重复提交
调整项目结构（exotic-harpoon/pom.xml包括所有依赖）
配置中心读取git

