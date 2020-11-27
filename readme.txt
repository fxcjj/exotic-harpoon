
1. 分析eviction-interval-timer-in-ms, lease-renewal-interval-in-seconds, lease-expiration-duration-in-seconds
eureka01配置为：
eureka:
    server:
      # 自我保护模式，默认打开（当出现网络分区导致丢失过多客户端时，不删除任何微服务）
      enable-self-preservation: false
      # eureka server清理无效节点的时间间隔，默认60000毫秒，即60秒
      eviction-interval-timer-in-ms: 10000

order模块配置为：
eureka:
  client:
    # 开启健康检查
    healthcheck:
      enabled: true
  instance:
    # eureka client发送心跳给server端的频率。如果在leaseExpirationDurationInSeconds后，server端没有收到client的心跳，则将摘除该instance
    lease-renewal-interval-in-seconds: 30
    # 告知服务端N秒还未收到心跳的话，就将该服务移除列表
    # eureka server至上一次收到client的心跳之后，等待下一次心跳的超时时间，在这个时间内若没收到下一次心跳，则将移除该instance
    lease-expiration-duration-in-seconds: 19

依次启动eureka, config, order模块，观察日志如下：

# config模块注册到了eureka server
2020-11-27 19:51:10.533  INFO 2536 --- [nio-8761-exec-2] c.n.e.registry.AbstractInstanceRegistry  : Registered instance CONFIG/192.168.141.78:config:8091 with status UP (replication=false)
# 清理无效节点（每隔10秒执行一次）
2020-11-27 19:51:14.239  INFO 2536 --- [a-EvictionTimer] c.n.e.registry.AbstractInstanceRegistry  : Running the evict task with compensationTime 0ms
2020-11-27 19:51:24.252  INFO 2536 --- [a-EvictionTimer] c.n.e.registry.AbstractInstanceRegistry  : Running the evict task with compensationTime 12ms
2020-11-27 19:51:34.267  INFO 2536 --- [a-EvictionTimer] c.n.e.registry.AbstractInstanceRegistry  : Running the evict task with compensationTime 14ms
2020-11-27 19:51:41.324  WARN 2536 --- [nio-8761-exec-5] c.n.e.registry.AbstractInstanceRegistry  : No remote registry available for the remote region us-east-1
2020-11-27 19:51:44.267  INFO 2536 --- [a-EvictionTimer] c.n.e.registry.AbstractInstanceRegistry  : Running the evict task with compensationTime 0ms

# order模块发送心跳，注册到eureka server
2020-11-27 19:51:51.572  INFO 2536 --- [nio-8761-exec-9] c.n.e.registry.AbstractInstanceRegistry  : Registered instance ORDER/192.168.141.78:order:8090 with status UP (replication=false)
# 第1次清理节点（时间: 19:51:54）
2020-11-27 19:51:54.283  INFO 2536 --- [a-EvictionTimer] c.n.e.registry.AbstractInstanceRegistry  : Running the evict task with compensationTime 15ms
2020-11-27 19:52:00.019  WARN 2536 --- [-CacheFillTimer] c.n.e.registry.AbstractInstanceRegistry  : No remote registry available for the remote region us-east-1
# 第2次清理节点（时间: 19:52:04）
2020-11-27 19:52:04.288  INFO 2536 --- [a-EvictionTimer] c.n.e.registry.AbstractInstanceRegistry  : Running the evict task with compensationTime 5ms
# 第3次清理节点（时间: 19:52:14），距离注册同order模块已过去20秒
# 当前order模块配置，发送心跳频率: lease-renewal-interval-in-seconds为30，心跳超时时间为: lease-expiration-duration-in-seconds为19。
# 即order模块下一次发送心跳时间在19:52:21，但此刻eureka server在19秒内未收到client心跳，所以会ereuka server的evict task会清理掉这个节点
2020-11-27 19:52:14.291  INFO 2536 --- [a-EvictionTimer] c.n.e.registry.AbstractInstanceRegistry  : Running the evict task with compensationTime 3ms
# 清理节点
2020-11-27 19:52:14.291  INFO 2536 --- [a-EvictionTimer] c.n.e.registry.AbstractInstanceRegistry  : Evicting 1 items (expired=1, evictionLimit=1)
2020-11-27 19:52:14.292  WARN 2536 --- [a-EvictionTimer] c.n.e.registry.AbstractInstanceRegistry  : DS: Registry: expired lease for ORDER/192.168.141.78:order:8090
# 移除order实例
2020-11-27 19:52:14.296  INFO 2536 --- [a-EvictionTimer] c.n.e.registry.AbstractInstanceRegistry  : Cancelled instance ORDER/192.168.141.78:order:8090 (replication=false)

2020-11-27 19:52:21.166  WARN 2536 --- [nio-8761-exec-8] c.n.e.registry.AbstractInstanceRegistry  : DS: Registry: lease doesn't exist, registering resource: ORDER - 192.168.141.78:order:8090
2020-11-27 19:52:21.167  WARN 2536 --- [nio-8761-exec-8] c.n.eureka.resources.InstanceResource    : Not Found (Renew): ORDER - 192.168.141.78:order:8090

# order模块发送心跳，注册到eureka server
2020-11-27 19:52:21.182  INFO 2536 --- [nio-8761-exec-6] c.n.e.registry.AbstractInstanceRegistry  : Registered instance ORDER/192.168.141.78:order:8090 with status UP (replication=false)
# 第1次清理节点（时间: 19:52:24）
2020-11-27 19:52:24.298  INFO 2536 --- [a-EvictionTimer] c.n.e.registry.AbstractInstanceRegistry  : Running the evict task with compensationTime 6ms
2020-11-27 19:52:30.030  WARN 2536 --- [-CacheFillTimer] c.n.e.registry.AbstractInstanceRegistry  : No remote registry available for the remote region us-east-1
# 第２次清理节点（时间: 19:52:34）
2020-11-27 19:52:34.301  INFO 2536 --- [a-EvictionTimer] c.n.e.registry.AbstractInstanceRegistry  : Running the evict task with compensationTime 2ms
# 第3次清理节点（时间: 19:52:44）
2020-11-27 19:52:44.309  INFO 2536 --- [a-EvictionTimer] c.n.e.registry.AbstractInstanceRegistry  : Running the evict task with compensationTime 7ms
# 清理节点
2020-11-27 19:52:44.309  INFO 2536 --- [a-EvictionTimer] c.n.e.registry.AbstractInstanceRegistry  : Evicting 1 items (expired=1, evictionLimit=1)
2020-11-27 19:52:44.309  WARN 2536 --- [a-EvictionTimer] c.n.e.registry.AbstractInstanceRegistry  : DS: Registry: expired lease for ORDER/192.168.141.78:order:8090
# 移除order实例
2020-11-27 19:52:44.309  INFO 2536 --- [a-EvictionTimer] c.n.e.registry.AbstractInstanceRegistry  : Cancelled instance ORDER/192.168.141.78:order:8090 (replication=false)
2020-11-27 19:52:51.214  WARN 2536 --- [nio-8761-exec-7] c.n.e.registry.AbstractInstanceRegistry  : DS: Registry: lease doesn't exist, registering resource: ORDER - 192.168.141.78:order:8090
2020-11-27 19:52:51.214  WARN 2536 --- [nio-8761-exec-7] c.n.eureka.resources.InstanceResource    : Not Found (Renew): ORDER - 192.168.141.78:order:8090

# order模块发送心跳，注册到eureka server
2020-11-27 19:52:51.230  INFO 2536 --- [nio-8761-exec-2] c.n.e.registry.AbstractInstanceRegistry  : Registered instance ORDER/192.168.141.78:order:8090 with status UP (replication=false)
2020-11-27 19:52:54.314  INFO 2536 --- [a-EvictionTimer] c.n.e.registry.AbstractInstanceRegistry  : Running the evict task with compensationTime 5ms
2020-11-27 19:53:00.046  WARN 2536 --- [-CacheFillTimer] c.n.e.registry.AbstractInstanceRegistry  : No remote registry available for the remote region us-east-1
2020-11-27 19:53:04.317  INFO 2536 --- [a-EvictionTimer] c.n.e.registry.AbstractInstanceRegistry  : Running the evict task with compensationTime 2ms
2020-11-27 19:53:14.319  INFO 2536 --- [a-EvictionTimer] c.n.e.registry.AbstractInstanceRegistry  : Running the evict task with compensationTime 2ms
2020-11-27 19:53:14.319  INFO 2536 --- [a-EvictionTimer] c.n.e.registry.AbstractInstanceRegistry  : Evicting 1 items (expired=1, evictionLimit=1)
2020-11-27 19:53:14.319  WARN 2536 --- [a-EvictionTimer] c.n.e.registry.AbstractInstanceRegistry  : DS: Registry: expired lease for ORDER/192.168.141.78:order:8090
2020-11-27 19:53:14.319  INFO 2536 --- [a-EvictionTimer] c.n.e.registry.AbstractInstanceRegistry  : Cancelled instance ORDER/192.168.141.78:order:8090 (replication=false)
2020-11-27 19:53:21.253  WARN 2536 --- [nio-8761-exec-6] c.n.e.registry.AbstractInstanceRegistry  : DS: Registry: lease doesn't exist, registering resource: ORDER - 192.168.141.78:order:8090
2020-11-27 19:53:21.253  WARN 2536 --- [nio-8761-exec-6] c.n.eureka.resources.InstanceResource    : Not Found (Renew): ORDER - 192.168.141.78:order:8090




