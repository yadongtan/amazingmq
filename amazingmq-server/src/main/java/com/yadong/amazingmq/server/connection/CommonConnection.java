package com.yadong.amazingmq.server.connection;

import com.yadong.amazingmq.server.channel.Channel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author YadongTan
 * @date 2022/9/6 10:53
 * @Description 一个连接
 */
public class CommonConnection implements Connection {

    // key = 通道id, value = Channel
    ConcurrentHashMap<Short, Channel> channelMap
            = new ConcurrentHashMap<>();

    private short connectionId;

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
        channelMap.remove(channelId);
    }

    @Override
    public ConcurrentHashMap<Short, Channel> getChannelMap() {
        return channelMap;
    }

}
