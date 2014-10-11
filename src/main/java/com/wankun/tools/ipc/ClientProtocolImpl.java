package com.wankun.tools.ipc;

import java.io.IOException;

import org.apache.hadoop.ipc.ProtocolSignature;

/**
 * 实现RPC协议
 * 
 * @author wankun
 * 
 */
public class ClientProtocolImpl implements ClientProtocol {

	@Override
	public long getProtocolVersion(String protocol, long clientVersion)
			throws IOException {
		return 0;
	}

	@Override
	public ProtocolSignature getProtocolSignature(String protocol,
			long clientVersion, int clientMethodsHash) throws IOException {
		return null;
	}

	@Override
	public String echo(String value) {
		return value;
	}

	@Override
	public int add(int v1, int v2) {
		return v1 + v2;
	}

}
