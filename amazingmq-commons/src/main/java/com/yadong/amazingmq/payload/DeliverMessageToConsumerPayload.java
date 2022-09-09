package com.yadong.amazingmq.payload;


import com.yadong.amazingmq.frame.Message;

public class DeliverMessageToConsumerPayload {

    private Message message;

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }
}
