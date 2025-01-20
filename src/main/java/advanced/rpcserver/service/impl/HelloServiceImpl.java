package advanced.rpcserver.service.impl;

import advanced.rpcserver.service.HelloService;

public class HelloServiceImpl implements HelloService {
    @Override
    public String hello(String name) {
        return name+" hello!!";
    }
}
