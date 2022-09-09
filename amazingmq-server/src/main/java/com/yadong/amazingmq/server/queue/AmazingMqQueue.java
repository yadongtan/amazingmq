package com.yadong.amazingmq.server.queue;

import com.yadong.amazingmq.frame.Message;
import com.yadong.amazingmq.server.channel.Channel;
import com.yadong.amazingmq.server.vhost.VirtualHost;

import java.util.Map;

/**
* @author YadongTan
* @date 2022/9/4 23:17
* @Description the super class of Queues
*/
public interface AmazingMqQueue {


    public String getQueueName();

    public AmazingMqQueue setQueueName(String queueName);

    public boolean isDurable();

    public AmazingMqQueue setDurable(boolean durable) ;

    public boolean isExclusive();

    public AmazingMqQueue setExclusive(boolean exclusive);

    public boolean isAutoDelete();

    public AmazingMqQueue setAutoDelete(boolean autoDelete);

    public VirtualHost getVhost();

    public AmazingMqQueue setVhost(VirtualHost vhost);

    public Map<String, Object> getArguments();

    public AmazingMqQueue setArguments(Map<String, Object> arguments);

    public boolean offer(Message message) throws OutOfMaxLengthException;

    public boolean addChannelListener(Channel channel);

    public boolean removeChannelListener(Channel channel);

    public boolean trySendMessageToConsumer();

    public boolean hashMessage();

}
