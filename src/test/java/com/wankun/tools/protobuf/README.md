
## 环境准备

### 测试版本

	protobuf git version : 2.5.0
	protobuf java version : 2.5.0 (这里采用hadoop-2.5.0-cdh5.2.0 版本所使用protocbuf测试)

### 安装步骤

	* ProtoBuf git 地址：https://github.com/google/protobuf  （version: 2.6+）
		                https://code.google.com/p/protobuf/downloads/list  （version: 2.5-）
	* 安装依赖工具： automake autoconf gcc gcc-c++ libtool等
	* ./autogen.sh
	* ./configure 
	* make
	* make install
	* smoke test : protoc --version

	> 可能用到：googletest.googlecode.com   84.15.64.35

## protobuf 使用

1. 编写proto文件

	package test;
	message Person {
		required string name = 1;
		required int32 id = 2;
		optional string email = 3;
	}
	
2. 编译成目标文件

	protoc ./src/main/proto/people.proto --java_out=./src/test/java/

3. 编写测试程序
	
	maven 依赖包
	
		<dependency>
			<groupId>com.google.protobuf</groupId>
			<artifactId>protobuf-java</artifactId>
			<version>2.5.0</version>
		</dependency>
	
	测试程序
		[TestPeople.java](TestPeople.java)，程序测试类对象的创建,write和read等操作
		
### API 使用
	
消息体实例的生成需要由对应的类的Build来生成，或直接从流中读取出来
	
## 使用protobuf 完成rpc调用

0. 实现原理

Server端

* 启动一个socket负责接收数据和发送数据，数据格式为header + body格式，header中记录消息的长度。
* 请求消息体中包含方法名称和方法的参数，请求消息体通过getDescriptorForType 方法可以获得声明类型，通过findMethodByName方法找到rpc要执行的方法。
* 通过rpc服务的实现类调用rpc对应的方法获取结果。
* 执行结果转换为数组发送会client端
	
Client端

* 连接服务端
* 组装消息体byte array
* 发送消息长度和消息体
* 接收消息长度和消息体
* 消息消息结果
	
核心 BlockingService

* getDescriptorForType 根据第一个参数，从所有Descriptor中找到对应的方法
* callBlockingMethod 负责调用指定方法
* getRequestPrototype
* getResponsePrototype
		
BlockingInterface 定义服务方法，子类负责具体实现，由CalculatorService.newReflectiveBlockingService方法，直接生成上述控制类
		

1. 定义消息格式和服务

	CalculatorMsg.proto 消息格式
	Calculator.proto 消息服务

2. 生成java文件
	
	cd ./src/main/proto/
	
	protoc ./src/main/proto/CalculatorMsg.proto --java_out=./src/main/java/
	protoc ./CalculatorMsg.proto --java_out=../java/
	protoc ./Calculator.proto --java_out=../java/
	
