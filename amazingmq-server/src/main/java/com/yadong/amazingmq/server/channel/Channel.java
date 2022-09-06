package com.yadong.amazingmq.server.channel;


// 一个Connection有多个Channel...
public interface Channel {

    short getChannelId();

    void setChannelId(short channelId);

}
