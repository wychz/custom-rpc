package com.wyc.pro.rpc.loadbalance;

import com.wyc.pro.rpc.extension.SPI;

import java.util.List;

@SPI
public interface LoadBalance {
    String selectServiceAddress(List<String> serviceAddress, String rpcServiceName);
}
