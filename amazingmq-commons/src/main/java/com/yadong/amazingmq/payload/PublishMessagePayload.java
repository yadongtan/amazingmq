package com.yadong.amazingmq.payload;

import com.yadong.amazingmq.frame.Message;

public class PublishMessagePayload implements Payload{

    private String exchangeName;

    private String routingKey;

    private Message message;

    public String getExchangeName() {
        return exchangeName;
    }

    public PublishMessagePayload setExchangeName(String exchangeName) {
        this.exchangeName = exchangeName;
        return this;
    }

    public String getRoutingKey() {
        return routingKey;
    }

    public PublishMessagePayload setRoutingKey(String routingKey) {
        this.routingKey = routingKey;
        return this;
    }

    public Message getMessage() {
        return message;
    }

    public PublishMessagePayload setMessage(Message message) {
        this.message = message;
        return this;
    }
}
