package com.yadong.amazingmq.server.exchange;

public class HeaderExchange  extends AbstractExchange{

    public HeaderExchange(String exchangeName, String exchangeType, boolean duration) {
        super(exchangeName, exchangeType, duration);
    }

    @Override
    public boolean sendMessageToQueue(String routingKey) {
        return false;

    }
}
