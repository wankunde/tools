package com.wankun.tools.ipc;

import org.apache.hadoop.ipc.VersionedProtocol;

/**
 * 定义RPC协议
 * 
 * @author wankun
 * 
 */
public interface ClientProtocol extends VersionedProtocol {

	public static final long versionID = 1L;

	String echo(String value);

	int add(int v1, int v2);

}
