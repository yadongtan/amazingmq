package com.yadong.amazingmq.server.queue;

import java.util.Map;

public class CommonAmazingMqQueue extends AbstractAmazingMqQueue {

    public CommonAmazingMqQueue(String queueName, boolean autoDelete, boolean durable, boolean exclusive, Map<String, Object> arguments) {
        super(queueName, autoDelete, durable, exclusive, arguments);
    }

}
