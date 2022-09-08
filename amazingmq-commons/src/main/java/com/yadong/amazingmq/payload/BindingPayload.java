package com.yadong.amazingmq.payload;

public class BindingPayload implements Payload{

    private String queueName;
    private String exchangeName;
    private String routingKey;

    public String getQueueName() {
        return queueName;
    }

    public BindingPayload setQueueName(String queueName) {
        this.queueName = queueName;
        return this;
    }

    public String getExchangeName() {
        return exchangeName;
    }

    public BindingPayload setExchangeName(String exchangeName) {
        this.exchangeName = exchangeName;
        return this;
    }

    public String getRoutingKey() {
        return routingKey;
    }

    public BindingPayload setRoutingKey(String routingKey) {
        this.routingKey = routingKey;
        return this;
    }
}
