package com.yadong.amazingmq.server.exchange;

import com.yadong.amazingmq.frame.Message;
import com.yadong.amazingmq.server.queue.AmazingMqQueue;
import com.yadong.amazingmq.server.queue.OutOfMaxLengthException;

public class FanoutExchange  extends AbstractExchange{
    public FanoutExchange(String exchangeName, String exchangeType, boolean duration) {
        super(exchangeName, exchangeType, duration);
    }


    @Override
    public boolean sendMessageToQueue(String routingKey, Message message) throws OutOfMaxLengthException {
        for (AmazingMqQueue queue : queueMap.values()) {
            queue.offer(message);
        }
        return true;
    }
}
