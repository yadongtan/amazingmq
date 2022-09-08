package com.yadong.amazingmq.server.channel;


import com.yadong.amazingmq.server.connection.Connection;

// 一个Connection有多个Channel...
public interface Channel {



    short getChannelId();

    void setChannelId(short channelId);

    public Connection getConnection();

    public void setConnection(Connection connection);
}
