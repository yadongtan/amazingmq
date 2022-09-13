package com.yadong.amazingmq.server.channel;


import com.yadong.amazingmq.frame.Message;
import com.yadong.amazingmq.server.connection.Connection;
import com.yadong.amazingmq.server.factory.FrameFactory;
import com.yadong.amazingmq.server.netty.handler.BrokerNettyHandler;
import com.yadong.amazingmq.server.queue.AmazingMqQueue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommonChannel implements Channel{

    private Connection connection;
    private short channelId;
    // 队列名字 - 队列
    private final Map<String, AmazingMqQueue> queueMap = new HashMap<>(); //这个channel连接到的queue

    @Override
    public short getChannelId() {
        return channelId;
    }

    @Override
    public void setChannelId(short channelId) {
        this.channelId = channelId;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void sendMessage(Message message) {
        BrokerNettyHandler client = connection.getClient();
        client.sendMessage(FrameFactory.createDeliverMsgToConsumerFrame(this, message));
    }

    public void addQueue(AmazingMqQueue queue) {
        queueMap.put(queue.getQueueName(), queue);
    }

    public void close(){
        queueMap.forEach((k, queue)->{
            queue.removeChannelListener(this);
        });
    }

    public Map<String, AmazingMqQueue> getQueueMap(){
        return queueMap;
    }
}
