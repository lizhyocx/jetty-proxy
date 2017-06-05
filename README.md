# jetty-proxy
## Jetty实现的反向代理

1、使用Jetty的NIO接收Http请求

2、使用Jetty的HttpClient异步转发请求，并将结果返回

3、要求请求转发需要透明传递，不能更改任何信息（参考AsyncProxyServlet实现，增强扩展性）

4、jetty支持http2，接收https请求

    a、升级jetty版本为9.4.x，JDK版本为：1.8，alpn版本依赖jdk版本
    b、start脚本添加alpn启动项：-Xbootclasspath/p:
    c、conf中配置证书信息

## 打包编译方式：
mvn clean package

## 部署方式：
解压jetty-proxy-assembly/target/jetty-proxy-assembly-1.0.zip,
执行bin/start.sh，启动程序，
执行bin/shutdown.sh 停止程序

程序相关配置信息在conf目录下

程序输出的日志在logs目录下

## 使用说明
通过请求：curl "http://localhost:8888?j_dis_url=https://www.baidu.com"
将请求转发至www.baidu.com

转发的url通过在url中的参数**j_dis_url**来配置