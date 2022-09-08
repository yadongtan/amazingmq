package com.yadong.amazingmq.client.channel;

import com.yadong.amazingmq.client.connection.Connection;
import com.yadong.amazingmq.client.netty.handler.BrokerNettyClient;


public class AMQChannel implements Channel{

    private short channelId;
    private final Connection connection;
    private final BrokerNettyClient client;



    public AMQChannel(Integer channelId, Connection connection, BrokerNettyClient client){
        this.connection = connection;
        this.channelId = channelId.shortValue();
        this.client = client;
    }

    @Override
    public int getChannelNumber() {
        return channelId;
    }

    @Override
    public Connection getConnection() {
        return connection;
    }

    @Override
    public boolean exchangeDeclare(String exchangeName, String mode, String duration) {
        return false;
    }
}
