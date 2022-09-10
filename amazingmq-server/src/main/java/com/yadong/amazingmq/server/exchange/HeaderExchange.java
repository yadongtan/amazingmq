package com.yadong.amazingmq.server.exchange;

import com.yadong.amazingmq.frame.Message;
import com.yadong.amazingmq.server.queue.AmazingMqQueue;
import com.yadong.amazingmq.server.queue.OutOfMaxLengthException;

/**
* @author YadongTan
* @date 2022/9/10 11:00
* @Description 暂不提供此类头部信息交换机的支持,反正这种性能也不会多高
*/
public class HeaderExchange  extends AbstractExchange{

    public HeaderExchange(String exchangeName, String exchangeType, boolean duration) {
        super(exchangeName, exchangeType, duration);
    }


    @Override
    public boolean sendMessageToQueue(String routingKey, Message message) throws OutOfMaxLengthException {
        AmazingMqQueue queue = queueMap.get(routingKey);
        if(queue == null)
            return false;
        return queue.offer(message);
    }
}
