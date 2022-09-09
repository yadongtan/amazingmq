package com.yadong.amazingmq.payload;



public class ConsumeMessagePayload implements Payload {

    private String queueName;

    public String getQueueName() {
        return queueName;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }
}
