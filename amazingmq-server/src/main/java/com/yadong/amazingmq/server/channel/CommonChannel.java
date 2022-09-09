package com.yadong.amazingmq.server.channel;


import com.yadong.amazingmq.frame.Message;
import com.yadong.amazingmq.server.connection.Connection;
import com.yadong.amazingmq.server.factory.FrameFactory;
import com.yadong.amazingmq.server.netty.handler.BrokerNettyHandler;

public class CommonChannel implements Channel{

    private Connection connection;
    private short channelId;

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
}
