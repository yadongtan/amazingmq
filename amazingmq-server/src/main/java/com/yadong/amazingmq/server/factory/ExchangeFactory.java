package com.yadong.amazingmq.server.factory;

import com.yadong.amazingmq.frame.Frame;
import com.yadong.amazingmq.server.exchange.Exchange;
import com.yadong.amazingmq.server.factory.AbstractBrokerFactory;


public class ExchangeFactory extends AbstractBrokerFactory {
    ExchangeFactory(Frame frame) {
        super(frame);
    }

    @Override
    public Exchange create() {
        return null;
    }
}
