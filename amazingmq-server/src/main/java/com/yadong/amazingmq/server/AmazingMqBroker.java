package com.yadong.amazingmq.server;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yadong.amazingmq.property.HostInfo;
import com.yadong.amazingmq.server.cluster.AmazingMqClusterApplication;
import com.yadong.amazingmq.server.netty.BrokerNettyServer;
import com.yadong.amazingmq.server.property.BrokerProperties;
import com.yadong.amazingmq.server.property.UserProperties;
import com.yadong.amazingmq.server.vhost.VirtualHost;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author YadongTan
 * @date 2022/9/6 8:12
 * @Description 单例, 当前AmazingMq实例的入口
 */
public class AmazingMqBroker {

    private static final Logger logger = LoggerFactory.getLogger(AmazingMqBroker.class);

    @JsonIgnore
    private static final AmazingMqBroker _INSTANCE;
    @JsonIgnore
    private static final String PREFIX = Objects.requireNonNull(AmazingMqBroker.class.getResource("/")).getPath() + "/";
    @JsonIgnore
    public static final boolean ENABLE_WEB = true; //是否开启web状态显示
    @JsonIgnore
    public static final boolean ENABLE_CLUSTER = true; //是否开启集群模式/镜像队列


    // host,port
    private BrokerProperties brokerProperties;
    // username -  UserProperties
    private ConcurrentHashMap<String, UserProperties> userPropertiesMap;
    // vhost路径 - vhost
    private ConcurrentHashMap<String, VirtualHost>
            virtualHostMap;


    static {
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
        _INSTANCE.brokerProperties = new BrokerProperties(new HostInfo("127.0.0.1", 7001), new HostInfo("127.0.0.1", 17001));
        AmazingMqClusterApplication.getInstance().getClusterHosts().add(new HostInfo("127.0.0.1", 17000));
        //启动队列调度
        //QueueScheduler.getInstance().startScheduler();
    }


    public static void main(String[] args) {
        AmazingMqBroker.getInstance().start();
        if (ENABLE_WEB) {
            SpringApplication.run(BrokerApplication.class, args);
        }
        if(ENABLE_CLUSTER) {
            AmazingMqClusterApplication.getInstance().run();
        }
    }


    private AmazingMqBroker() {
    }

    public static AmazingMqBroker getInstance() {
        return _INSTANCE;
    }

    public void start() {
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
