package com.wyc.pro.rpc.transport.handler;

import com.wyc.pro.common.SingletonFactory;
import com.wyc.pro.common.exception.RpcException;
import lombok.extern.slf4j.Slf4j;
import com.wyc.pro.rpc.provider.ServiceProvider;
import com.wyc.pro.rpc.provider.ServiceProviderImpl;
import com.wyc.pro.rpc.transport.dto.RpcRequest;

import java.lang.reflect.Method;

@Slf4j
public class RpcRequestHandler {
    private final ServiceProvider serviceProvider;

    public RpcRequestHandler() {
        serviceProvider = SingletonFactory.getInstance(ServiceProviderImpl.class);
    }

    public Object handle(RpcRequest rpcRequest) {
        Object service = serviceProvider.getService(rpcRequest.toRpcProperties());
        return invokeTargetMethod(rpcRequest, service);
    }

    private Object invokeTargetMethod(RpcRequest rpcRequest, Object service) {
        Object result;
        try {
            Method method = service.getClass().getMethod(rpcRequest.getMethodName(), rpcRequest.getParamTypes());
            result = method.invoke(service, rpcRequest.getParameters());
            log.info("service:[{}] successful invoke method: [{}]", rpcRequest.getInterfaceName(), rpcRequest.getMethodName());
        } catch (Exception e) {
            throw new RpcException(e.getMessage(), e);
        }
        return result;
    }
}
