package rpc.register;

import java.net.InetSocketAddress;

public interface ServiceDiscovery {
    InetSocketAddress lookupService(String rpcServiceName);
}
