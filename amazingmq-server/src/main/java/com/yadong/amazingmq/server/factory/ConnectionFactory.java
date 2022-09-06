package com.yadong.amazingmq.server.factory;

import com.yadong.amazingmq.frame.Frame;
import com.yadong.amazingmq.payload.ConnectionPayload;
import com.yadong.amazingmq.server.connection.Connection;
import com.yadong.amazingmq.server.factory.AbstractBrokerFactory;
import com.yadong.amazingmq.utils.ObjectMapperUtils;

public class ConnectionFactory extends AbstractBrokerFactory {

    public Connection create(Frame frame){
        String payload = frame.getPayload();
        ConnectionPayload connectionPayload = ObjectMapperUtils.toObject(payload, ConnectionPayload.class);
        return null;
    }

}
