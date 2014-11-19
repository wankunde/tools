#Hadoop RPC

## 程序说明
本例为测试hadoop2 中的RPC框架

* ClientProtocol 定义RPC的接口协议
* ClientProtocolImpl 定义RPC的接口的实现
* MyServer 根据RPC的接口协议和接口协议的实现启动一个对外提供的服务
* MyClient 客户端想指定服务端发送请求，服务端提供调用服务，并返回结果


## 实现原理
	客户端代理对象主要指定代理接口类，代理接口类负责连接到服务端，在server端指定protocol的实现类，来对外提供服务。

## Hadoop RPC流程分析

一、代理Proxy对象获取
入口：
ClientProtocol proxy = RPC.getProxy(ClientProtocol.class,ClientProtocol.versionID, addr, conf);
1.1 获取ProtocolEngine
	static synchronized RpcEngine getProtocolEngine(Class<?> protocol,Configuration conf) 
	在PROTOCOL_ENGINES map中没有找到对应的RpcEngine，取默认class org.apache.hadoop.ipc.WritableRpcEngine，接着调用Engine的getProxy方法获取代理。
1.2    代理对象生成
  T proxy = (T) Proxy.newProxyInstance(protocol.getClassLoader(), 
							     new Class[] { protocol }, 
							     new Invoker(protocol, addr, ticket, conf,factory, rpcTimeout));
       在proxy代理是通过protocol的反射生成实例的，在反射的时候调用了Invoker实现类，负责修改具体的实现方法。在Invoker的构造方法，有实例化两个对象，ConnectionId类的实例和client实例。ConnectionId实例会调用Client的getConnectionId方法中生成新的ConnectionId remoteId和 Client client对象，来获取server端的连接；
	remoteId代表Client中的一个远程连接；
（在客户端维护了一个Map clients，缓存client实例）
1.3 构造proxy对象
	proxy对象包含一个RpcEngine$Invoker，RpcEngine$Invoker中包含三个对象Client，isClosed,remoteId。

二、RPC方法调用
2.1 代理方法执行
	在调用proxy对象的方法时，会执行WritableRpcEngine中的invoke方法，invoke参数为(proxy对象,Method 需要执行的方法,args执行方法的参数)
2.2 客户端发送方法
	ObjectWritable value = (ObjectWritable)client.call(RPC.RpcKind.RPC_WRITABLE, new Invocation(method, args), remoteId);
在这里会将方法，参数包装为一个Invocation对象，也是后面的rpcRequest，Invocation类继承了Writable接口，Configurable接口，可以进行序列化传递。

2.2.1 封装请求对象Invocation
	在Invocation对象的生产过程中会包装RPC版本，协议名等信息。
	
2.2.2 创建call对象
	createCall方法。
2.2.3 获取连接
	getConnection(remoteId, call, serviceClass);
2.2.3.1 在connections连接池中，根据remoteId创建真实的物理连接
2.2.3.2 在connection的calls队列中添加call，并notify当前client对象本身，这样在client对象上等待的程序就可以运行了。
      	calls.put(call.id, call);
      	notify();
2.2.3.3 包装socket的输入和输出，实现数据的截取
2.2.4   connection.sendRpcRequest(call); send rpc request
2.2.4.1 向new出来的DataOutput中写入各种rpc信息（协议版本，协议名，调用方法，客户端版本，客户端方法hash值等），还有调用方法的参数，这里用到了io包中ObjectWritable方法类序列化传输参数。在ObjectWritable中还有相关的UTF8的数据写入等。
2.3   通过ExecutorService，启动新的线程发送请求，并捕获返回结果在senderFuture中
在该线程中将DataOutputBuffer中的数据写入到DataOutputStream中并flush，这样可以保证数据的一次性写入的输出流中。实现多个线程共享一个输出流而数据相互不影响，通过synchronized锁住输出流out实现。
2.4 call如果没有执行完毕，则进行wait，等待调用结束
2.5 call的返回结果rpcResponse
