package com.yadong.amazingmq.server.channel;


import com.yadong.amazingmq.frame.Message;
import com.yadong.amazingmq.server.connection.Connection;
import com.yadong.amazingmq.server.queue.AmazingMqQueue;

import java.util.Map;

// 一个Connection有多个Channel...
public interface Channel {

    short getChannelId();

    void setChannelId(short channelId);

    public Connection getConnection();

    public void setConnection(Connection connection);

    public void sendMessage(Message message);

    public void close();

    public void addQueue(AmazingMqQueue queue);

    public Map<String, AmazingMqQueue> getQueueMap();
}
