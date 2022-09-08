package com.yadong.amazingmq.server.exchange;

public class FanoutExchange  extends AbstractExchange{
    public FanoutExchange(String exchangeName, String exchangeType, boolean duration) {
        super(exchangeName, exchangeType, duration);
    }

    @Override
    public boolean sendMessageToQueue(String routingKey) {
        return false;

    }
}
