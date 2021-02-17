package rpc.transport.client;

import rpc.extension.SPI;
import rpc.transport.dto.RpcRequest;

@SPI
public interface RpcRequestTransport {
    Object sendRpcRequest(RpcRequest rpcRequest);
}
