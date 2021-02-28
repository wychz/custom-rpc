package com.wyc.pro.rpc.register;

import com.wyc.pro.rpc.extension.SPI;

import java.net.InetSocketAddress;

@SPI
public interface ServiceRegistry {

    void registerService(String rpcServiceName, InetSocketAddress inetSocketAddress);
}
