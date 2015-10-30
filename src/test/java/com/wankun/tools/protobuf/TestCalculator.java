package com.wankun.tools.protobuf;

import com.wankun.tools.protobuf.CalculatorMsg.RequestProto;
import com.wankun.tools.protobuf.CalculatorMsg.ResponseProto;
import org.junit.Test;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class TestCalculator {

	public int doTest(String op, int a, int b) {
		// TODO Auto-generated method stub
		Socket s = null;
		DataOutputStream out = null;
		DataInputStream in = null;
		int ret = 0;
		try {
			s = new Socket("localhost", 9999);
			out = new DataOutputStream(s.getOutputStream());
			in = new DataInputStream(s.getInputStream());

			RequestProto.Builder builder = RequestProto.newBuilder();
			builder.setMethodName(op);
			builder.setNum1(a);
			builder.setNum2(b);
			RequestProto request = builder.build();

			byte[] bytes = request.toByteArray();
			out.writeInt(bytes.length);
			out.write(bytes);
			out.flush();

			int dataLen = in.readInt();
			byte[] data = new byte[dataLen];
			int count = in.read(data);
			if (count != dataLen) {
				System.err.println("something bad happened!");
			}

			ResponseProto result = ResponseProto.parseFrom(data);
			System.out.println(a + " " + op + " " + b + "=" + result.getResult());
			ret = result.getResult();

		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(e.toString());
		} finally {
			try {
				in.close();
				out.close();
				s.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return ret;
	}

	@Test
	public void add() {
		org.junit.Assert.assertEquals(5, doTest("add", 2, 3));
	}

	@Test
	public void minus() {
		org.junit.Assert.assertEquals(7, doTest("minus", 10, 3));
	}

}