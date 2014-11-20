package com.wankun.tools.protobuf;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.google.protobuf.BlockingService;

public class CalculatorService implements CalculatorInt {

	private Server server = null;
	private final Class protocol = CalculatorInt.class;
	private final ClassLoader classLoader = Thread.currentThread() 
			.getContextClassLoader();
	private final String protoPackage = "com.wankun.tools.protobuf";
	private final String host = "localhost";
	private final int port = 9999;

	public CalculatorService() {

	}

	@Override
	public int add(int a, int b) {
		return a + b;
	}

	public int minus(int a, int b) {
		return a - b;
	}

	public void init() {
		CalculatorPB service = new CalculatorPBServiceImpl(this);
		Calculator cal= new Calculator();

		/*
		 * interface: org.tao.pbtest.server.CalculatorPB
		 */
		Class<?> pbProtocol = service.getClass().getInterfaces()[0];

		/*
		 * class: org.tao.pbtest.proto.Calculator$CalculatorService
		 */
		Class<?> protoClazz = Calculator.class;

		Method method = null;
		try {

			// pbProtocol.getInterfaces()[] 即是接口
			// org.tao.pbtest.proto.Calculator$CalculatorService$BlockingInterface

			method = protoClazz.getMethod("newReflectiveBlockingService",
					pbProtocol.getInterfaces()[0]);
			method.setAccessible(true);
		} catch (NoSuchMethodException e) {
			System.err.print(e.toString());
		}

		// 通过反射设定协议中，实际的服务类实例
		try {
			server = new Server(pbProtocol, (BlockingService) method.invoke(null, service), port);
			server.start();
		} catch (InvocationTargetException e) {
		} catch (IllegalArgumentException e) {
		} catch (IllegalAccessException e) {
		}
	}

	/*
	 * 
	 * return org.tao.pbtest.server.api.CalculatorPBServiceImpl
	 * 
	 */
	public Class<?> getPbServiceImplClass2() {
		String packageName = protocol.getPackage().getName();
		String className = protocol.getSimpleName();
		String pbServiceImplName = packageName + "." + className
				+ "PBServiceImpl";
		Class<?> clazz = null;
		try {
			clazz = Class.forName(pbServiceImplName, true, classLoader);
		} catch (ClassNotFoundException e) {
			System.err.println(e.toString());
		}
		return clazz;
	}

	/*
	 * return org.tao.pbtest.proto.Calculator$CalculatorService
	 */
	public Class<?> getProtoClass() {
		String className = protocol.getSimpleName();
		String protoClazzName = protoPackage + "." + className + "$"
				+ className + "Service";
		Class<?> clazz = null;
		try {
			clazz = Class.forName(protoClazzName, true, classLoader);
		} catch (ClassNotFoundException e) {
			System.err.println(e.toString());
		}
		return clazz;
	}

	public static void main(String[] args) {
		CalculatorService cs = new CalculatorService();
		cs.init();
	}
}