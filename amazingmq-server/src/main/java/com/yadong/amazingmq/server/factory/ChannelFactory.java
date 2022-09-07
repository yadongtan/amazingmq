package com.yadong.amazingmq.server.factory;

import com.yadong.amazingmq.frame.Frame;
import com.yadong.amazingmq.server.channel.Channel;
import com.yadong.amazingmq.server.factory.AbstractBrokerFactory;

public class ChannelFactory extends AbstractBrokerFactory {

    ChannelFactory(Frame frame) {
        super(frame);
    }

    @Override
    public Channel create() {
        return null;
    }

}
