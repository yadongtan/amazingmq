package com.yadong.amazingmq.server.factory;

import com.yadong.amazingmq.frame.Frame;
import com.yadong.amazingmq.payload.ExchangeDeclarePayload;
import com.yadong.amazingmq.payload.Payload;
import com.yadong.amazingmq.server.AmazingMqBroker;
import com.yadong.amazingmq.server.exchange.*;
import com.yadong.amazingmq.server.netty.handler.BrokerNettyHandler;
import com.yadong.amazingmq.server.vhost.AuthErrorException;
import com.yadong.amazingmq.utils.ObjectMapperUtils;


public class ExchangeFactory extends AbstractBrokerFactory {
    ExchangeFactory(Frame frame) {
        super(frame);
    }

    @Override
    public Exchange create(BrokerNettyHandler client) {
        ExchangeDeclarePayload payload = ObjectMapperUtils.toObject(frame.getPayload(), ExchangeDeclarePayload.class);
        String exchangeName = payload.getExchangeName();
        //这个交换机已经存在了,不再创建
        if(client.getConnection().getVirtualHost().getExchange(exchangeName)!=null){
            return null;
        }
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

    @Override
    public Exchange create(String virtualHost) throws AuthErrorException {
        ExchangeDeclarePayload payload = ObjectMapperUtils.toObject(frame.getPayload(), ExchangeDeclarePayload.class);
        String exchangeName = payload.getExchangeName();
        //这个交换机已经存在了,不再创建
        if(AmazingMqBroker.getInstance().getVirtualHostMap().get(virtualHost).getExchange(exchangeName)!=null){
            return null;
        }
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


    @Override
    public Exchange create(String virtualHost, Payload payload) throws AuthErrorException {
        ExchangeDeclarePayload exchangeDeclarePayload = (ExchangeDeclarePayload) payload;
        String exchangeName = exchangeDeclarePayload.getExchangeName();
        //这个交换机已经存在了,不再创建
        if(AmazingMqBroker.getInstance().getVirtualHostMap().get(virtualHost).getExchange(exchangeName)!=null){
            return null;
        }
        String exchangeType = exchangeDeclarePayload.getExchangeType();
        boolean duration = exchangeDeclarePayload.isDuration();
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
