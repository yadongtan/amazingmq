package com.yadong.amazingmq.server.factory;

import com.yadong.amazingmq.frame.Frame;
import com.yadong.amazingmq.server.queue.Queue;

public class QueueFactory extends AbstractBrokerFactory{


    QueueFactory(Frame frame) {
        super(frame);
    }

    @Override
    public Queue create() {
        return null;
    }

}
