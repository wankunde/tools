package com.wankun.tools.thrift;

import org.apache.thrift.TException;
import org.apache.thrift.transport.TSSLTransportFactory;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TSSLTransportFactory.TSSLTransportParameters;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
 
public class JavaClient {
	public static void main(String[] args) {

//		if (args.length != 1) {
//			System.out.println("Please enter 'simple' or 'secure'");
//			System.exit(0);
//		}

		try {
			TTransport transport;
//			if (args[0].contains("simple")) {
//				transport = new TSocket("localhost", 9090);
//				transport.open();
//			} else {
//				TSSLTransportParameters params = new TSSLTransportParameters();
//				params.setTrustStore("../../lib/java/test/.truststore", "thrift", "SunX509", "JKS");
//				transport = TSSLTransportFactory.getClientSocket("localhost", 9091, 0, params);
//			}
			transport = new TSocket("localhost", 9090);
			transport.open();

			TProtocol protocol = new TBinaryProtocol(transport);
			Calculator.Client client = new Calculator.Client(protocol);

			perform(client);

			transport.close();
		} catch (TException x) {
			x.printStackTrace();
		}
	}

	private static void perform(Calculator.Client client) throws TException {

		int sum = client.add(1, 1);
		System.out.println("1+1=" + sum);

	}
}