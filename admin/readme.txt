1. 访问地址
http://localhost:8097


当eureka server中配置的lease-expiration-duration-in-seconds
小于
eureka client中配置的lease-renewal-interval-in-seconds时。
在eureka页面（http://localhost:8761）会出现以下：
RENEWALS ARE LESSER THAN THE THRESHOLD. THE SELF PRESERVATION MODE IS TURNED OFF. THIS MAY NOT PROTECT INSTANCE EXPIRY IN CASE OF NETWORK/OTHER PROBLEMS.
