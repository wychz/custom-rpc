package com.wyc.pro.serviceImpl;

import com.wyc.pro.Hello;
import com.wyc.pro.HelloService;
import com.wyc.pro.common.annotation.RpcService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RpcService(group = "test1", version = "version1")
public class HelloServiceImpl1 implements HelloService {

    static {
        System.out.println("HelloServiceImpl1被创建");
    }

    @Override
    public String hello(Hello hello) {
        log.info("HelloServiceImpl1收到： {}", hello.getMessage());
        String result = "Hello description is " + hello.getDescription();
        log.info("HelloServiceImpl1返回： {}", result);
        return result;
    }
}
