package com.yadong.amazingmq.server.connection;


import com.yadong.amazingmq.server.channel.Channel;
import com.yadong.amazingmq.server.exchange.Exchange;
import com.yadong.amazingmq.server.vhost.VirtualHost;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public interface Connection {

    public short getConnectionId();

    public void setConnectionId(short cid);

    public void addChannel(Channel channel);

    public void removeChannel(short channelId);

    public ConcurrentHashMap<Short, Channel> getChannelMap();

    public VirtualHost getVirtualHost();

    public void setVirtualHost(VirtualHost virtualHost);

}
