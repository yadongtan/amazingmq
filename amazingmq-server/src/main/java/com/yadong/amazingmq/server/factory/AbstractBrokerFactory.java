package com.yadong.amazingmq.server.factory;

import com.yadong.amazingmq.frame.Frame;
import com.yadong.amazingmq.server.connection.Connection;
import com.yadong.amazingmq.server.netty.handler.BrokerNettyHandler;
import com.yadong.amazingmq.server.vhost.AuthErrorException;

public abstract class AbstractBrokerFactory {

    protected Frame frame;

    AbstractBrokerFactory(Frame frame){
        this.frame = frame;
    }

    abstract public Object create(BrokerNettyHandler client) throws AuthErrorException;

}
