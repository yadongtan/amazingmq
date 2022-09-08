package com.yadong.amazingmq.server.exchange;

import com.yadong.amazingmq.server.queue.Queue;

import java.util.Iterator;

public class DirectExchange extends AbstractExchange{
    public DirectExchange(String exchangeName, String exchangeType, boolean duration) {
        super(exchangeName, exchangeType, duration);
    }

    @Override
    public boolean sendMessageToQueue(String routingKey) {
        Queue queue = queueMap.get(routingKey);
        if(queue == null)
            return false;
        return true;
    }
}
