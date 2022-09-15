package com.yadong.amazingmq.proxy;


import com.yadong.amazingmq.property.HostInfo;
import com.yadong.amazingmq.proxy.properties.ProxyProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * @author YadongTan
 * @date 2022/9/14 14:57
 * @Description 提供代理, 负载均衡转发到各个AmazingMq服务器上
 */
public class AmazingProxy {

    public static final String loadBalanceStrategy = "random";

    private static final AmazingProxy _INSTANCE = new AmazingProxy();

    private final List<HostInfo> hostList = new ArrayList<>();

    public List<HostInfo> getHostList() {
        return hostList;
    }

    {
        hostList.add(new HostInfo("localhost",7000));
        //hostList.add(new HostInfo("localhost",7001));
    }

    private AmazingProxy(){}

    public static AmazingProxy getInstance(){
        return _INSTANCE;
    }

    public void startProxy(){
        ProxyNettyServer.getInstance().syncStart(new ProxyProperties("localhost", 36666));
    }

    public static void main(String[] args) {
        AmazingProxy.getInstance().startProxy();
    }

}
