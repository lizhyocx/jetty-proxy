# jetty-proxy
Jetty实现的反向代理

1、使用Jetty的NIO接收Http请求

2、使用Jetty的HttpClient异步转发请求，并将结果返回

3、要求请求转发需要透明传递，不能更改任何信息（参考AsyncProxyServlet实现，增强扩展性）

打包编译方式：
mvn clean package

部署方式：
解压jetty-proxy-assembly/target/jetty-proxy-assembly-1.0.zip,
执行bin/start.sh，启动程序

程序相关配置信息在conf目录下