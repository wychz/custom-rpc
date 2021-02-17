package rpc.Server;

import common.RpcServiceProperties;
import common.SingletonFactory;
import lombok.SneakyThrows;
import rpc.config.CustomShutdownHook;
import rpc.provider.ServiceProvider;
import rpc.provider.ServiceProviderImpl;

import java.net.InetAddress;

public class NettyRpcServer {
    public static final int PORT = 9998;
    private final ServiceProvider serviceProvider = SingletonFactory.getInstance(ServiceProviderImpl.class);

    public void registerService(Object service, RpcServiceProperties rpcServiceProperties) {
        serviceProvider.publishService(service, rpcServiceProperties);
    }

    @SneakyThrows
    public void start() {
        CustomShutdownHook.getCustomShutdownHook().clearAll();
        String host = InetAddress.getLocalHost().getHostAddress();
        EventLoopGroup
    }

}
