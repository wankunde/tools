package com.wankun.tools.protobuf;

import com.google.protobuf.BlockingService;
import com.google.protobuf.Descriptors.MethodDescriptor;
import com.google.protobuf.Message;
import com.wankun.tools.protobuf.Calculator.CalculatorService;
import com.wankun.tools.protobuf.CalculatorMsg.RequestProto;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread {

	private BlockingService impl;
	private ServerSocket ss;
	private static final int port = 9999;

	public static void main(String[] args) {
		Server server = new Server(CalculatorService.newReflectiveBlockingService(new CalculatorPBServiceImpl()));
		server.start();
	}

	public Server(BlockingService impl) {
		this.impl = impl;
		try {
			this.ss = new ServerSocket(port);
		} catch (IOException e) {
		}
	}

	public void run() {
		Socket clientSocket = null;
		DataOutputStream dos = null;
		DataInputStream dis = null;

		int testCount = 10; // 进行10次计算后就退出
		while (testCount-- > 0) {
			try {
				clientSocket = ss.accept();
				dos = new DataOutputStream(clientSocket.getOutputStream());
				dis = new DataInputStream(clientSocket.getInputStream());
				int dataLen = dis.readInt();
				byte[] dataBuffer = new byte[dataLen];
				int readCount = dis.read(dataBuffer);
				byte[] result = processOneRpc(dataBuffer);

				dos.writeInt(result.length);
				dos.write(result);
				dos.flush();
			} catch (Exception e) {
			}
		}
		try {
			dos.close();
			dis.close();
		} catch (Exception e) {
		}
		;

	}

	public byte[] processOneRpc(byte[] data) throws Exception {
		RequestProto request = RequestProto.parseFrom(data);
		String methodName = request.getMethodName();
		MethodDescriptor methodDescriptor = impl.getDescriptorForType().findMethodByName(methodName);
		Message response = impl.callBlockingMethod(methodDescriptor, null, request);
		return response.toByteArray();
	}
}