package com.yadong.amazingmq.client.consumer;

import com.sun.xml.internal.messaging.saaj.soap.Envelope;
import com.yadong.amazingmq.client.channel.Channel;

import java.io.IOException;

public class DefaultConsumer implements Consumer{

    private final Channel _channel;
    private volatile String _consumerTag;


    public DefaultConsumer(Channel _channel) {
        this._channel = _channel;
    }

    public Channel getChannel() {
        return this._channel;
    }

    public String getConsumerTag() {
        return this._consumerTag;
    }

    @Override
    public void handleDelivery(String consumerTag, Envelope envelope, byte[] body) throws IOException {

    }

    @Override
    public void handleConsumeOk(String consumerTag) {
        this._consumerTag = consumerTag;
    }


}
