package com.yadong.amazingmq.server.connection;


import com.yadong.amazingmq.server.channel.Channel;

import java.util.List;

public interface Connection {

    public short getConnectionId();
    public void setConnectionId(short cid);
    public void addChannel(Channel channel);
    public void removeChannel(short channelId);
}
