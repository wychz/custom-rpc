import com.wyc.pro.HelloService;
import com.wyc.pro.common.RpcServiceProperties;
import com.wyc.pro.common.annotation.RpcScan;
import com.wyc.pro.rpc.transport.server.NettyRpcServer;
import com.wyc.pro.serviceImpl.HelloServiceImpl2;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@RpcScan(basePackage = {"com.wyc.pro"})
public class NettyServerMain {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(NettyServerMain.class);
        NettyRpcServer nettyRpcServer = (NettyRpcServer) applicationContext.getBean("nettyRpcServer");
        HelloService helloService2 = new HelloServiceImpl2();
        RpcServiceProperties rpcServiceProperties = RpcServiceProperties.builder().group("test2").version("version2").build();
        nettyRpcServer.registerService(helloService2, rpcServiceProperties);
        nettyRpcServer.start();
    }
}
