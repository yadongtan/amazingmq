package com.yadong.amazingmq.server.queue;

import com.yadong.amazingmq.server.vhost.VirtualHost;

import java.util.Map;

public class CommonAmazingMqQueue extends AbstractAmazingMqQueue {

    public CommonAmazingMqQueue(String queueName, boolean autoDelete, boolean durable, boolean exclusive, Map<String, Object> arguments, VirtualHost virtualHost) {
        super(queueName, autoDelete, durable, exclusive, arguments, virtualHost);
    }

}
