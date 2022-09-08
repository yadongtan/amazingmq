package com.yadong.amazingmq.server.exchange;

public class DirectExchange extends AbstractExchange{
    public DirectExchange(String exchangeName, String exchangeType, boolean duration) {
        super(exchangeName, exchangeType, duration);
    }
}
