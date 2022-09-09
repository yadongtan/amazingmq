package com.yadong.amazingmq.client.consumer;




import com.yadong.amazingmq.frame.Envelope;

import java.io.IOException;

public interface Consumer {

    public void handleDelivery(String consumerTag, Envelope envelope, byte[] body) throws IOException ;
    public void handleConsumeOk(String consumerTag) ;
}
