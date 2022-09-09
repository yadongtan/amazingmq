package com.yadong.amazingmq.server.factory;

import com.yadong.amazingmq.frame.Frame;
import com.yadong.amazingmq.payload.CreateChannelPayload;
import com.yadong.amazingmq.server.channel.Channel;
import com.yadong.amazingmq.server.channel.CommonChannel;
import com.yadong.amazingmq.server.connection.Connection;
import com.yadong.amazingmq.server.netty.handler.BrokerNettyHandler;
import com.yadong.amazingmq.utils.ObjectMapperUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChannelFactory extends AbstractBrokerFactory {
    private static final Logger logger = LoggerFactory.getLogger(ChannelFactory.class);


    ChannelFactory(Frame frame) {
        super(frame);
    }

    @Override
    public Channel create(BrokerNettyHandler client) {
        CreateChannelPayload payload = ObjectMapperUtils.toObject(frame.getPayload(), CreateChannelPayload.class);
        // 创建信道
        Channel channel = new CommonChannel();
        channel.setChannelId(payload.getChannelId());
        return channel;
    }

}
