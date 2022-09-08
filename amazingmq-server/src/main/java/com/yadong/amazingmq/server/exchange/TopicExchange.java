package com.yadong.amazingmq.server.exchange;

public class TopicExchange extends AbstractExchange{

    public TopicExchange(String exchangeName, String exchangeType, boolean duration) {
        super(exchangeName, exchangeType, duration);
    }

    @Override
    public boolean sendMessageToQueue(String routingKey) {
        return false;
    }
}
