package com.yadong.amazingmq.server.factory;

import com.yadong.amazingmq.frame.Frame;

public class BrokerFactoryProducer{

    public static AbstractBrokerFactory createFactory(Frame frame){
        // create abstractFactory in order to create different components of the broker
        short type = frame.getType();
        // create connection
        if(type == Frame.PayloadType.CREATE_CONNECTION.getType()){
            return new ConnectionFactory(frame);
        }else if(type == Frame.PayloadType.CREATE_CHANNEL.getType()){
            return new ChannelFactory(frame);
        }
        return null;
    }

}
