package com.yadong.amazingmq.payload;

import java.util.HashMap;
import java.util.Map;

public class QueueDeclarePayload implements Payload {

    private String queueName;
    private boolean durable;
    private boolean exclusive;
    private boolean autoDelete;
    private Map<String, Object> arguments;

    public String getQueueName() {
        return queueName;
    }

    public QueueDeclarePayload setQueueName(String queueName) {
        this.queueName = queueName;
        return this;
    }

    public boolean isDurable() {
        return durable;
    }

    public QueueDeclarePayload setDurable(boolean durable) {
        this.durable = durable;
        return this;
    }

    public boolean isExclusive() {
        return exclusive;
    }

    public QueueDeclarePayload setExclusive(boolean exclusive) {
        this.exclusive = exclusive;
        return this;
    }

    public boolean isAutoDelete() {
        return autoDelete;
    }

    public QueueDeclarePayload setAutoDelete(boolean autoDelete) {
        this.autoDelete = autoDelete;
        return this;
    }

    public Map<String, Object> getArguments() {
        return arguments;
    }

    public QueueDeclarePayload setArguments(Map<String, Object> arguments) {
        this.arguments = arguments;
        return this;
    }
}
