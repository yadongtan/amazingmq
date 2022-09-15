package com.yadong.amazingmq.server;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yadong.amazingmq.property.HostInfo;
import com.yadong.amazingmq.server.cluster.ClusterNettyClient;
import com.yadong.amazingmq.server.cluster.ClusterNettyServer;
import com.yadong.amazingmq.server.cluster.handler.ClusterClientHandler;
import com.yadong.amazingmq.server.netty.BrokerNettyServer;
import com.yadong.amazingmq.server.property.BrokerProperties;
import com.yadong.amazingmq.server.property.UserProperties;
import com.yadong.amazingmq.server.vhost.VirtualHost;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class AmazingMqBroker2 {

    private static final Logger logger = LoggerFactory.getLogger(AmazingMqBroker2.class);

    @JsonIgnore
    private static final AmazingMqBroker2 _INSTANCE;
    @JsonIgnore
    private static final String PREFIX = Objects.requireNonNull(AmazingMqBroker.class.getResource("/")).getPath() + "/";
    @JsonIgnore
    private static final boolean ENABLE_WEB = true; //是否开启web状态显示
    @JsonIgnore
    private static final boolean ENABLE_CLUSTER = true; //是否开启集群模式/镜像队列

    // host,port
    private BrokerProperties brokerProperties;
    // username -  UserProperties
    private ConcurrentHashMap<String, UserProperties> userPropertiesMap;
    // vhost路径 - vhost
    private ConcurrentHashMap<String, VirtualHost>
            virtualHostMap;
    // 集群主机
    private List<HostInfo> clusterHosts; //不包含主机


    static {
        _INSTANCE = new AmazingMqBroker2();
        // 创建默认vhost
        _INSTANCE.virtualHostMap = new ConcurrentHashMap<>();
        _INSTANCE.virtualHostMap.put("/", new VirtualHost("/"));

        // 创建默认guest
        UserProperties userProperties = new UserProperties("guest", "guest");
        userProperties.addVhost("/");
        _INSTANCE.userPropertiesMap = new ConcurrentHashMap<>();
        _INSTANCE.userPropertiesMap.put(userProperties.getUsername(), userProperties);

        // 设置Broker默认配置
        _INSTANCE.brokerProperties = new BrokerProperties(new HostInfo("127.0.0.1", 7001), new HostInfo("127.0.0.1",17001));

        // 创建
        _INSTANCE.clusterHosts = new ArrayList<>();
        _INSTANCE.clusterHosts.add(new HostInfo("127.0.0.1",17000));    //集群中的Server
        //启动队列调度
        //QueueScheduler.getInstance().startScheduler();
    }


    public static void main(String[] args) {
        AmazingMqBroker2.getInstance().start();
        if (ENABLE_WEB) {
            SpringApplication.run(BrokerApplication.class, args);
        }
        if(ENABLE_CLUSTER) {
            // 开放本机集群通信端口
            ClusterNettyServer.getInstance().syncStart(AmazingMqBroker2.getInstance().getBrokerProperties());
            // 连接集群
            AmazingMqBroker2.getInstance().connectCluster();
        }

    }

    private void connectCluster(){
        List<HostInfo> clusterHosts = AmazingMqBroker2.getInstance().getClusterHosts();
        for (HostInfo clusterHost : clusterHosts) {
            ClusterClientHandler client = null;
            while(client == null) {
                try {
                    client = ClusterNettyClient.createAndConnect(clusterHost);
                } catch (InterruptedException e) {
                    logger.info("连接集群服务器:[" + clusterHost + "] 失败, 正在重连...");
                }
            }
        }
    }

    private AmazingMqBroker2() {
    }

    public static AmazingMqBroker2 getInstance() {
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

    public List<HostInfo> getClusterHosts() {
        return clusterHosts;
    }

    public void setClusterHosts(List<HostInfo> clusterHosts) {
        this.clusterHosts = clusterHosts;
    }


}
