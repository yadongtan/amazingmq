package com.yadong.amazingmq.server.factory;

import com.yadong.amazingmq.frame.Frame;
import com.yadong.amazingmq.server.netty.handler.BrokerNettyHandler;

public class BrokerFactoryProducer{

    public static AbstractBrokerFactory createFactory(Frame frame){
        // create abstractFactory in order to create different components of the broker
        short type = frame.getType();
        // create connection
        if(type == Frame.PayloadType.CREATE_CONNECTION.getType()){
            return new ConnectionFactory(frame);
        }else if(type == Frame.PayloadType.CREATE_CHANNEL.getType()){
            return new ChannelFactory(frame);
        }else if(type == Frame.PayloadType.EXCHANGE_DECLARED.getType()){
            return new ExchangeFactory(frame);
        }else if(type == Frame.PayloadType.QUEUE_DECLARED.getType()){
            return new QueueFactory(frame);
        }else if(type == Frame.PayloadType.BINDING_DECLARED.getType()){
            return new BindingFactory(frame);
        }
        return null;
    }

}
