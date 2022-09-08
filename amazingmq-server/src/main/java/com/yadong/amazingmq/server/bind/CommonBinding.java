package com.yadong.amazingmq.server.bind;

public class CommonBinding implements Binding{

    private String queueName;
    private String exchangeName;
    private String routingKey;

    public String getQueueName() {
        return queueName;
    }

    public CommonBinding setQueueName(String queueName) {
        this.queueName = queueName;
        return this;
    }

    public String getExchangeName() {
        return exchangeName;
    }

    public CommonBinding setExchangeName(String exchangeName) {
        this.exchangeName = exchangeName;
        return this;
    }

    public String getRoutingKey() {
        return routingKey;
    }

    public CommonBinding setRoutingKey(String routingKey) {
        this.routingKey = routingKey;
        return this;
    }
}
