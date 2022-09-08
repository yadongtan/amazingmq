package com.yadong.amazingmq.server.factory;

import com.yadong.amazingmq.frame.Frame;
import com.yadong.amazingmq.payload.ExchangeDeclarePayload;
import com.yadong.amazingmq.server.exchange.*;
import com.yadong.amazingmq.utils.ObjectMapperUtils;


public class ExchangeFactory extends AbstractBrokerFactory {
    ExchangeFactory(Frame frame) {
        super(frame);
    }

    @Override
    public Exchange create() {
        ExchangeDeclarePayload payload = ObjectMapperUtils.toObject(frame.getPayload(), ExchangeDeclarePayload.class);
        String exchangeName = payload.getExchangeName();
        String exchangeType = payload.getExchangeType();
        boolean duration = payload.isDuration();
        switch (exchangeType){
            case "direct":
                return new DirectExchange(exchangeName, exchangeType, duration);
            case "fanout":
                return new FanoutExchange(exchangeName, exchangeType, duration);
            case "topic":
                return new TopicExchange(exchangeName, exchangeType, duration);
            case "header":
                return new HeaderExchange(exchangeName, exchangeType, duration);
        }
        return null;
    }
}
