#Thrift 

测试Thrift通信

## 开发流程
	
	1. 安装Thrift工具
	2. 编写Thrift接口协议文件(sample1.thrift，其中定义了add服务)
	3. 生成通信协议 (Calculator.java )
	4. 编写协议的实现(CalculatorHandler.java ，完成具体的服务实现)
	5. 启动服务(JavaServer.java，通过一个线程实例对外提供服务)
	6. 客户端调用(JavaClient.java)
		6.1 建立连接 TTransport
		6.2 由连接生成Calculator.Client 调用实例
		6.3 执行远程调用
		6.4 关闭连接