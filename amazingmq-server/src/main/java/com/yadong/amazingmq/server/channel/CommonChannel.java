package com.yadong.amazingmq.server.channel;


import com.yadong.amazingmq.server.connection.Connection;

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
}
