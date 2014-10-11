#Hadoop RPC

本例为测试hadoop2 中的RPC框架

* ClientProtocol 定义RPC的接口协议
* ClientProtocolImpl 定义RPC的接口的实现
* MyServer 根据RPC的接口协议和接口协议的实现启动一个对外提供的服务
* MyClient 客户端想指定服务端发送请求，服务端提供调用服务，并返回结果