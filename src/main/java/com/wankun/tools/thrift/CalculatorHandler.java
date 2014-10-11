package com.wankun.tools.thrift;

public class CalculatorHandler implements Calculator.Iface {

	public int add(int n1, int n2) {
		System.out.println("add(" + n1 + "," + n2 + ")");
		return n1 + n2;
	}

}
