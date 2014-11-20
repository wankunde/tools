package com.wankun.tools.protobuf;

import com.google.protobuf.RpcController;
import com.google.protobuf.ServiceException;
import com.wankun.tools.protobuf.Calculator.CalculatorService.BlockingInterface;
import com.wankun.tools.protobuf.CalculatorMsg.RequestProto;
import com.wankun.tools.protobuf.CalculatorMsg.ResponseProto;

public class CalculatorPBServiceImpl implements BlockingInterface {

	@Override
	public ResponseProto add(RpcController controller, RequestProto request) throws ServiceException {
		// TODO Auto-generated method stub
		ResponseProto proto = ResponseProto.getDefaultInstance();
		ResponseProto.Builder build = ResponseProto.newBuilder();
		int num1 = request.getNum1();
		int num2 = request.getNum2();
		int res = num1+num2;
		ResponseProto result = null;
		build.setResult(res);
		result = build.build();
		return result;
	}

	@Override
	public ResponseProto minus(RpcController controller, RequestProto request) throws ServiceException {
		// TODO Auto-generated method stub
		ResponseProto proto = ResponseProto.getDefaultInstance();
		ResponseProto.Builder build = ResponseProto.newBuilder();
		int num1 = request.getNum1();
		int num2 = request.getNum2();
		int res = num1-num2;
		ResponseProto result = null;
		build.setResult(res);
		result = build.build();
		return result;
	}

}
