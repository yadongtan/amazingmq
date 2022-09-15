package com.yadong.amazingmq.server.cluster;

import com.yadong.amazingmq.frame.Frame;
import com.yadong.amazingmq.payload.cluster.ClusterMessageRemovedPayload;
import com.yadong.amazingmq.payload.cluster.ClusterTransmitPayload;
import com.yadong.amazingmq.property.HostInfo;
import com.yadong.amazingmq.server.AmazingMqBroker;
import com.yadong.amazingmq.server.cluster.handler.ClusterClientHandler;
import com.yadong.amazingmq.utils.ObjectMapperUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
* @author YadongTan
* @date 2022/9/15 14:35
* @Description 提供集群功能
*/
public class AmazingMqClusterApplication {

    private static final Logger logger = LoggerFactory.getLogger(AmazingMqClusterApplication.class);

    private static AmazingMqClusterApplication _INSTANCE;


    // 集群主机
    private List<HostInfo> clusterHosts; //不包含主机
    // 已经与集群建立连接的主机信息
    private List<ClusterClientHandler> clusterClientHandlerList = new ArrayList<>();

    {
        clusterHosts = new ArrayList<>();
//        clusterHosts.add(new HostInfo("127.0.0.1",17000));    //集群中的Server
    }

    public static AmazingMqClusterApplication getInstance(){
        if(_INSTANCE == null){
            synchronized (AmazingMqClusterApplication.class){
                if(_INSTANCE == null){
                    _INSTANCE = new AmazingMqClusterApplication();
                }
            }
        }
        return _INSTANCE;
    }

    private AmazingMqClusterApplication(){}

    public void connectCluster(){
        for (HostInfo clusterHost : clusterHosts) {
            ClusterClientHandler client = null;
            while(client == null) {
                try {
                    client = ClusterNettyClient.createAndConnect(clusterHost);
                } catch (Exception e) {
                    logger.info("连接集群服务器:[" + clusterHost + "] 失败, 正在重连...");
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException interruptedException) {
                        interruptedException.printStackTrace();
                    }
                }
            }
            clusterClientHandlerList.add(client);
        }
    }

    // 启动集群模式
    public void run(){
        ClusterNettyServer.getInstance().syncStart(AmazingMqBroker.getInstance().getBrokerProperties());
        // 连接集群
        AmazingMqClusterApplication.getInstance().connectCluster();
    }

    //同步这些组件的创建与消息
    public void synchronizationClusterMetadata(Frame frame, String virtualHost){
        for (ClusterClientHandler clusterClientHandler : clusterClientHandlerList) {
            // 包装这个payload,并转发给集群中其他服务器
            frame.setPayload(ObjectMapperUtils.toJSON(new ClusterTransmitPayload(frame.getPayload(), virtualHost)));
            clusterClientHandler.send(frame);
        }
    }

    public boolean synchronizationRemoveMessage(String vhost, String queueName, int messageId) throws InterruptedException {
        Frame frame = new Frame();
        frame.setType(Frame.PayloadType.REMOVE_MESSAGE.getType());
        MessageResponseCount count = new MessageResponseCount(AmazingMqClusterApplication.getInstance().clusterHosts.size());
        for (ClusterClientHandler clusterClientHandler : clusterClientHandlerList) {
            // 包装这个payload,并转发给集群中其他服务器
            frame.setPayload(ObjectMapperUtils.toJSON(new ClusterMessageRemovedPayload(vhost, queueName, messageId)));
            clusterClientHandler.sendRemoveMessage(count, frame);
        }
        synchronized (count){
            count.wait();
        }
        return count.isOk();
    }



    public List<HostInfo> getClusterHosts() {
        return clusterHosts;
    }

    public void setClusterHosts(List<HostInfo> clusterHosts) {
        this.clusterHosts = clusterHosts;
    }
}
