package com.wankun.tools.ipc;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.ipc.RPC;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * 构造RPC Client 并发送RPC请求
 * 
 * @author wankun
 * 
 */
public class MyClient {

	public static void main(String[] args) throws IOException {
		Configuration conf = new Configuration();
		InetSocketAddress addr = new InetSocketAddress("localhost", 1008);
		ClientProtocol proxy = RPC.getProxy(ClientProtocol.class,
				ClientProtocol.versionID, addr, conf);
		int result = proxy.add(2, 3);
		String echoResult = proxy.echo("Hello RPC");
		System.out.println("add:" + result + "  echo :" + echoResult);
	}
}
