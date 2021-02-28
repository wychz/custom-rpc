package com.wyc.pro.rpc.transport.client;

import com.wyc.pro.rpc.extension.SPI;
import com.wyc.pro.rpc.transport.dto.RpcRequest;

@SPI
public interface RpcRequestTransport {
    Object sendRpcRequest(RpcRequest rpcRequest);
}
