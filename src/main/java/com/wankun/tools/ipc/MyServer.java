package com.wankun.tools.ipc;

import org.apache.hadoop.HadoopIllegalArgumentException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.ipc.RPC;
import org.apache.hadoop.ipc.Server;

import java.io.IOException;

/**
 * 构造并启动RPC Server
 * 
 * @author wankun
 * 
 */
public class MyServer {

	Server server;
	String ADDRESS = "localhost";

	public MyServer() throws HadoopIllegalArgumentException, IOException {
		Configuration conf = new Configuration();
		server = new RPC.Builder(conf).setProtocol(ClientProtocol.class)
				.setInstance(new ClientProtocolImpl()).setBindAddress(ADDRESS)
				.setPort(1008).setNumHandlers(5).build();
		server.start();
	}

	public static void main(String[] args) {
		try {
			MyServer myserver = new MyServer();
		} catch (HadoopIllegalArgumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
