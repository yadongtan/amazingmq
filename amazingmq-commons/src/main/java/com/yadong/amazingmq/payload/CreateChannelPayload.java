package com.yadong.amazingmq.payload;

public class CreateChannelPayload implements Payload{

    private short connectionId;
    private short channelId;

    public short getConnectionId() {
        return connectionId;
    }

    public void setConnectionId(short connectionId) {
        this.connectionId = connectionId;
    }

    public short getChannelId() {
        return channelId;
    }

    public void setChannelId(short channelId) {
        this.channelId = channelId;
    }
}
