package com.yadong.amazingmq.server.factory;

import com.yadong.amazingmq.frame.Frame;
import com.yadong.amazingmq.payload.ConnectionPayload;
import com.yadong.amazingmq.utils.ObjectMapperUtils;

public abstract class AbstractBrokerFactory {

    abstract public Object create(Frame frame);

}
