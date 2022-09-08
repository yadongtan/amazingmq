package com.yadong.amazingmq.server.queue;

import com.yadong.amazingmq.server.vhost.VirtualHost;

import java.util.HashMap;
import java.util.Map;

/**
* @author YadongTan
* @date 2022/9/4 23:17
* @Description the super class of Queues
*/
public interface Queue {


    public String getQueueName();

    public Queue setQueueName(String queueName);

    public boolean isDurable();

    public Queue setDurable(boolean durable) ;

    public boolean isExclusive();

    public Queue setExclusive(boolean exclusive);

    public boolean isAutoDelete();

    public Queue setAutoDelete(boolean autoDelete);

    public VirtualHost getVhost();

    public Queue setVhost(VirtualHost vhost);

    public Map<String, Object> getArguments();

    public Queue setArguments(Map<String, Object> arguments);
}
