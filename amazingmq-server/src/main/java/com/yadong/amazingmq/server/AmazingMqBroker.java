package com.yadong.amazingmq.server;


import com.yadong.amazingmq.server.netty.BrokerNettyServer;
import com.yadong.amazingmq.server.property.BrokerProperties;
import com.yadong.amazingmq.server.property.UserProperties;
import com.yadong.amazingmq.server.vhost.VirtualHost;
import org.springframework.boot.SpringApplication;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author YadongTan
 * @date 2022/9/6 8:12
 * @Description 单例, 当前AmazingMq实例的入口
 */
public class AmazingMqBroker {

    private static final AmazingMqBroker _INSTANCE;
    private static final String PREFIX = Objects.requireNonNull(AmazingMqBroker.class.getResource("/")).getPath() + "/";
    private static final boolean ENABLE_WEB = true;

    // host,port
    private BrokerProperties brokerProperties;
    // username -  UserProperties
    private ConcurrentHashMap<String,UserProperties> userPropertiesMap;
    // vhost路径 - vhost
    private ConcurrentHashMap<String, VirtualHost>
            virtualHostMap;

    static{
        _INSTANCE = new AmazingMqBroker();
        // 创建默认vhost
        _INSTANCE.virtualHostMap = new ConcurrentHashMap<>();
        _INSTANCE.virtualHostMap.put("/", new VirtualHost("/"));

        // 创建默认guest
        UserProperties userProperties = new UserProperties("guest", "guest");
        userProperties.addVhost("/");
        _INSTANCE.userPropertiesMap = new ConcurrentHashMap<>();
        _INSTANCE.userPropertiesMap.put(userProperties.getUsername(), userProperties);

        // 设置Broker默认配置
        _INSTANCE.brokerProperties = new BrokerProperties("127.0.0.1", 7000);

    }


    public static void main(String[] args) {
        AmazingMqBroker.getInstance().start();
        if(ENABLE_WEB){
            SpringApplication.run(BrokerApplication.class, args);
        }
        //boolean accessible = Auth.accessible(new UserProperties("guest", "guest", "/"));
    }




    private AmazingMqBroker(){}

    public static AmazingMqBroker getInstance(){
        return _INSTANCE;
    }

    public void start(){
        BrokerNettyServer.getInstance().syncStart(brokerProperties);
    }

    public BrokerProperties getBrokerProperties() {
        return brokerProperties;
    }

    public void setBrokerProperties(BrokerProperties brokerProperties) {
        this.brokerProperties = brokerProperties;
    }

    public ConcurrentHashMap<String, UserProperties> getUserPropertiesMap() {
        return userPropertiesMap;
    }

    public void setUserPropertiesMap(ConcurrentHashMap<String, UserProperties> userPropertiesMap) {
        this.userPropertiesMap = userPropertiesMap;
    }

    public ConcurrentHashMap<String, VirtualHost> getVirtualHostMap() {
        return virtualHostMap;
    }

    public void setVirtualHostMap(ConcurrentHashMap<String, VirtualHost> virtualHostMap) {
        this.virtualHostMap = virtualHostMap;
    }

}
