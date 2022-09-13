package com.yadong.amazingmq.payload;


public class ConnectionCreatedPayload {
    private short connectionId;

    public short getConnectionId() {
        return connectionId;
    }

    public void setConnectionId(short connection) {
        this.connectionId = connection;
    }
}
