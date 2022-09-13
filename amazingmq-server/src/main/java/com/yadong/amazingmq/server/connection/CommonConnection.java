package com.yadong.amazingmq.server.connection;

import com.yadong.amazingmq.server.AmazingMqBroker;
import com.yadong.amazingmq.server.channel.Channel;
import com.yadong.amazingmq.server.exchange.Exchange;
import com.yadong.amazingmq.server.netty.handler.BrokerNettyHandler;
import com.yadong.amazingmq.server.vhost.VirtualHost;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author YadongTan
 * @date 2022/9/6 10:53
 * @Description 一个连接
 */
public class CommonConnection implements Connection {


    private static final AtomicInteger connectionIdGenerator = new AtomicInteger(1);
    // key = 通道id, value = Channel
    ConcurrentHashMap<Short, Channel> channelMap
            = new ConcurrentHashMap<>();

    private short connectionId;
    private VirtualHost virtualHost;
    private BrokerNettyHandler client;

    public CommonConnection(VirtualHost virtualHost){
        connectionId = (short) connectionIdGenerator.getAndIncrement();
        while(virtualHost.getConnectionMap().containsKey(connectionId)){
            connectionId = (short) connectionIdGenerator.getAndIncrement();
        }
        this.virtualHost = virtualHost;
    }

    public BrokerNettyHandler getClient() {
        return client;
    }

    public void setClient(BrokerNettyHandler client) {
        this.client = client;
    }

    public VirtualHost getVirtualHost() {
        return virtualHost;
    }

    public void setVirtualHost(VirtualHost virtualHost) {
        this.virtualHost = virtualHost;
    }

    @Override
    public short getConnectionId() {
        return connectionId;
    }

    @Override
    public void setConnectionId(short cid) {
        this.connectionId = cid;
    }

    @Override
    public void addChannel(Channel channel){
        channelMap.put(channel.getChannelId(), channel);
    }

    @Override
    public void removeChannel(short channelId){
        Channel channel = channelMap.get(channelId);
        channel.close();
        channelMap.remove(channelId);
    }

    @Override
    public ConcurrentHashMap<Short, Channel> getChannelMap() {
        return channelMap;
    }

}
