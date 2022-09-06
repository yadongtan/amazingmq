package com.yadong.amazingmq.server.channel;

public class CommonChannel implements Channel{

    private short channelId;

    @Override
    public short getChannelId() {
        return channelId;
    }

    @Override
    public void setChannelId(short channelId) {
        this.channelId = channelId;
    }
}
