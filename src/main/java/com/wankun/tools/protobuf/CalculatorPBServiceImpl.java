package com.wankun.tools.protobuf;
  
import com.google.protobuf.RpcController;
import com.google.protobuf.ServiceException;
import com.wankun.tools.protobuf.CalculatorMsg.RequestProto;
import com.wankun.tools.protobuf.CalculatorMsg.ResponseProto;
  
public class CalculatorPBServiceImpl implements CalculatorPB {  
  
    public CalculatorInt real;  
      
    public CalculatorPBServiceImpl(CalculatorInt impl){  
        this.real = impl;  
    }  
      
    @Override
    public ResponseProto add(RpcController controller, RequestProto request) throws ServiceException {  
        // TODO Auto-generated method stub  
        ResponseProto proto = ResponseProto.getDefaultInstance();  
        ResponseProto.Builder build = ResponseProto.newBuilder();  
        int add1 = request.getNum1();  
        int add2 = request.getNum2();  
        int sum = real.add(add1, add2);  
        ResponseProto result = null;  
        build.setResult(sum);  
        result = build.build();  
        return result;  
    }  
  
    @Override  
    public ResponseProto minus(RpcController controller, RequestProto request) throws ServiceException {  
        // TODO Auto-generated method stub  
        ResponseProto proto = ResponseProto.getDefaultInstance();  
        ResponseProto.Builder build = ResponseProto.newBuilder();  
        int add1 = request.getNum1();  
        int add2 = request.getNum2();  
        int sum = real.minus(add1, add2);  
        ResponseProto result = null;  
        build.setResult(sum);  
        result = build.build();  
        return result;  
    }  
  
}  
